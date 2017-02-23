/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.saljex.sxlibrary.SXUtil;

/**
 *
 * @author Ulf
 */
public class KatalogServlet extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {

			boolean printAsKatalog = false;
			boolean tvingaBrutto = false;
			boolean printUndergrupp = false;
			if ("kat".equals(request.getParameter("view"))) {
				printAsKatalog = true;
				tvingaBrutto = true;
			} else if ("lst".equals(request.getParameter("view"))) {
				printAsKatalog = true;
				tvingaBrutto = false;				
			}
			if ("true".equals(request.getParameter("ug"))) printUndergrupp = true;
			
			
			boolean contentOnly = Const.getInitData(request).isContentOnlyCall();
			
			Integer grpid = null;
			String[] pathArr = request.getPathInfo() != null ? request.getPathInfo().split("/") : null;

			KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
			
			KatalogGrupp kg = null;
			KatalogGrupp avdelning=null;
			String logID=null;
			boolean showIndexPage=false;
			if (pathArr!=null) {
				try { 
					pathArr = request.getPathInfo().split("/");
					if ("index".equals(pathArr[1]))  {
						logID="index";
						showIndexPage=true;
						try {
							if (pathArr.length>1) avdelning = kgl.getGrupper().get(new Integer(pathArr[2]));
							kg = avdelning;
						} catch (Exception e) {showIndexPage=false;}
					} else {
						grpid = new Integer(pathArr[1]); 
						logID=pathArr[1];
						kg = kgl.getGrupper().get(grpid);
						if (kg==null) grpid=null;
					}
				} catch (Exception e) { grpid=null; } 
			} else  {
				grpid = 0;
				logID="0";
			}
			
			if (kg!=null && kg.isVisaundergrupper() ) printUndergrupp=true;

//			request.setAttribute(Const.ATTRIB_KATALOGGRUPPLISTA, kgl);
			request.setAttribute(Const.ATTRIB_KATALOGAVDELNING, avdelning);
			request.setAttribute(Const.ATTRIB_KATALOGREQUESTEDGRUPP, kg);
			if (grpid==null && !showIndexPage) {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
			}
			if (showIndexPage) {
				if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
				request.getRequestDispatcher("/WEB-INF/katalog-index.jsp").include(request, response);

			} else {

				if (grpid==null) {
					if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
					request.getRequestDispatcher("/WEB-INF/pagenotfound.jsp").include(request, response);				
				} else { 
					KatalogHeaderInfo khInfo = kgl.getKatalogHeaderInfo(grpid); 

					request.setAttribute(Const.ATTRIB_KATALOGHEADERINFO, khInfo);
					ArrayList<Produkt> prod = SQLHandler.getProdukterInGrupp(Const.getConnection(request), grpid, Const.getSessionData(request).getAvtalsKundnr());
					if (prod!=null && prod.size()>0) {
						Produkt p = prod.get(0);
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:title\" content=\"");
						Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(khInfo.getKatalogGrupp().getRubrik()));
						Const.getInitData(request).addExtraHTMLHeaderContent("\">");
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:type\" content=\"article\">");
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image\" content=\"");
						Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p,250));
						Const.getInitData(request).addExtraHTMLHeaderContent("\">");
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image:secure_url\" content=\"");
						Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p,250));
						Const.getInitData(request).addExtraHTMLHeaderContent("\">");
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:description\" content=\"");
						Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(khInfo.getKatalogGrupp().getText()));
						Const.getInitData(request).addExtraHTMLHeaderContent("\">");
						Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:url\" content=\"");
						StringBuffer reqURL = request.getRequestURL();
						String qString=request.getQueryString();
						if (qString!=null) {
							reqURL.append("?");
							reqURL.append(qString);
						}
						Const.getInitData(request).addExtraHTMLHeaderContent(reqURL.toString()); 
						Const.getInitData(request).addExtraHTMLHeaderContent("\">");
						
					}
					if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
					
					request.getRequestDispatcher("/WEB-INF/katalog-head.jsp").include(request, response);				
					if(!printUndergrupp) request.getRequestDispatcher("/WEB-INF/katalog-gruppchildren.jsp").include(request, response);				
					

					if ((prod==null || prod.size()<1) && !printUndergrupp) { 
						ArrayList<Produkt> rekProd = SQLHandler.getRekommenderadeToplistaInGrupp(Const.getConnection(request), grpid, Const.getSessionData(request).getAvtalsKundnr(), Const.getSessionData(request).getLagerNr(), 4);
						if (rekProd!=null && rekProd.size() > 0) {
							out.print("<h3>Rekommenderat</h3>");
							request.getRequestDispatcher("/WEB-INF/kbl-header.jsp").include(request, response);				
							for (Produkt p : rekProd) {
								request.setAttribute(Const.ATTRIB_PRODUKT, SQLHandler.getProdukt(Const.getConnection(request), p.getKlasid(), Const.getSessionData(request).getAvtalsKundnr()));
								request.getRequestDispatcher("/WEB-INF/kbl-block-content-small.jsp").include(request, response);				
							}
							request.getRequestDispatcher("/WEB-INF/kbl-footer.jsp").include(request, response);				
						}
					} else {
						if (printAsKatalog) 
							request.getRequestDispatcher("/WEB-INF/kat-header.jsp").include(request, response);	
						else 
							request.getRequestDispatcher("/WEB-INF/kbl-header.jsp").include(request, response);				
						
						printProdukt(request, response, prod, printAsKatalog, tvingaBrutto);
						if (printUndergrupp) { //Skriv undergrupper i en nivå
							for (Map.Entry<Integer, KatalogGrupp> entry : kgl.getGrupper().entrySet()) {
								if (grpid.equals(entry.getValue().getPrevGrpId())) {
									printUndergruppRubrik(out, entry.getValue().getRubrik());
									prod = SQLHandler.getProdukterInGrupp(Const.getConnection(request), entry.getValue().getGrpId(), Const.getSessionData(request).getAvtalsKundnr());
									printProdukt(request, response, prod, printAsKatalog, tvingaBrutto);
								}
							}
						}
						
						if (printAsKatalog)
							request.getRequestDispatcher("/WEB-INF/kat-footer.jsp").include(request, response);				
						else
							request.getRequestDispatcher("/WEB-INF/kbl-footer.jsp").include(request, response);				
							
					}
					
					request.getRequestDispatcher("/WEB-INF/katalog-foot.jsp").include(request, response);									
				}
			}

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			
			Const.loggaSidvisning(request, "katalog", logID);
			
			
		} catch (SQLException e) { e.printStackTrace(); throw new ServletException("SQL-Fel");}
	}
	
	private void printProdukt(HttpServletRequest request, HttpServletResponse response, ArrayList<Produkt> prod, boolean printAsKatalog, boolean tvingaBrutto) throws ServletException, IOException {
			if (prod==null || prod.size() < 1) return;
						for (Produkt p : prod) {
							request.setAttribute(Const.ATTRIB_PRODUKT, p);
							if (printAsKatalog) {
								request.setAttribute(Const.ATTRIB_PRINTASLISTPRIS, tvingaBrutto);
								request.getRequestDispatcher("/WEB-INF/kat-row.jsp").include(request, response);				
							} else
								request.getRequestDispatcher("/WEB-INF/kbl-block-content-small.jsp").include(request, response);				
								
						}
							
					
		
	}
	
	private void printUndergruppRubrik(PrintWriter out, String rubrik) {
		out.print("<h3>" + SXUtil.toHtml(rubrik) + "</h3>");
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
