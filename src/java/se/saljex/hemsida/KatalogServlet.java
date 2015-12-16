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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

			
			boolean contentOnly = Const.getInitData(request).isContentOnlyCall();
			
			Integer grpid = null;
			String[] pathArr = request.getPathInfo() != null ? request.getPathInfo().split("/") : null;

			KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
			
			KatalogGrupp kg = null;
			KatalogGrupp avdelning=null;
			boolean showIndexPage=false;
			if (pathArr!=null) {
				try { 
					pathArr = request.getPathInfo().split("/");
					if ("index".equals(pathArr[1]))  {
						showIndexPage=true;
						try {
							if (pathArr.length>1) avdelning = kgl.getGrupper().get(new Integer(pathArr[2]));
							kg = avdelning;
						} catch (Exception e) {showIndexPage=false;}
					} else {
						grpid = new Integer(pathArr[1]); 
						kg = kgl.getGrupper().get(grpid);
						if (kg==null) grpid=null;
					}
				} catch (Exception e) { grpid=null; } 
			} else  grpid = 0;
			

//			request.setAttribute(Const.ATTRIB_KATALOGGRUPPLISTA, kgl);
			request.setAttribute(Const.ATTRIB_KATALOGAVDELNING, avdelning);
			request.setAttribute(Const.ATTRIB_KATALOGREQUESTEDGRUPP, kg);
			if (grpid==null && !showIndexPage) {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
			}
			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
			if (showIndexPage) {
				request.getRequestDispatcher("/WEB-INF/katalog-index.jsp").include(request, response);

			} else {

				if (grpid==null) {
					request.getRequestDispatcher("/WEB-INF/pagenotfound.jsp").include(request, response);				
				} else {
					KatalogHeaderInfo khInfo = kgl.getKatalogHeaderInfo(grpid);

					request.setAttribute(Const.ATTRIB_KATALOGHEADERINFO, khInfo);
					request.getRequestDispatcher("/WEB-INF/katalog-gruppchildren.jsp").include(request, response);				

					ArrayList<Produkt> prod = SQLHandler.getProdukterInGrupp(Const.getConnection(request), grpid, Const.getSessionData(request).getAvtalsKundnr());
					if (prod==null || prod.size()<1) { 
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
						request.getRequestDispatcher("/WEB-INF/kbl-header.jsp").include(request, response);				
						for (Produkt p : prod) {
							request.setAttribute(Const.ATTRIB_PRODUKT, p);
							request.getRequestDispatcher("/WEB-INF/kbl-block-content-small.jsp").include(request, response);				
						}
						request.getRequestDispatcher("/WEB-INF/kbl-footer.jsp").include(request, response);				
					}
				}
			}

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			
			
			
			
			
			
			
			
		} catch (SQLException e) { e.printStackTrace(); throw new ServletException("SQL-Fel");}
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
