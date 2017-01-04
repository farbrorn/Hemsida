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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 */
public class OutletServlet extends HttpServlet {

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
			Integer underGrpid = null;
			boolean felaktigParameter = false;
			String[] pathArr = request.getPathInfo() != null ? request.getPathInfo().split("/") : null;
			if (pathArr!=null && pathArr.length > 1) {
				try {
					underGrpid = new Integer(pathArr[1]);
				} catch (NumberFormatException e) { felaktigParameter = true; }
			}
			KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
			
			String logID=null;
			logID = Const.getStartupData().getConfig("Hemsida-OutletGruppNr", "");
			if (logID.length()>0) grpid = new Integer(logID);  
			

//			KatalogHeaderInfo khInfo = null;
			List<KatalogGrupp> undergrupper = null; 
			boolean undergruppHittad=false;
			KatalogGrupp kg=null;
			if (grpid!=null ) {
//				khInfo = kgl.getKatalogHeaderInfo(grpid); 
				kg = SQLHandler.getGrupp(Const.getConnection(request), grpid);
				if (kg!=null) {
					undergrupper =  SQLHandler.getGrupperInGrupp(Const.getConnection(request), grpid);
					if (underGrpid!=null) for (KatalogGrupp g : undergrupper) {
						if (underGrpid.equals(g.getGrpId())) { undergruppHittad=true; break; }
					}
				}
			}
			

			if (grpid==null || (!undergruppHittad && underGrpid != null) || felaktigParameter) {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
				if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
				request.getRequestDispatcher("/WEB-INF/pagenotfound.jsp").include(request, response);				
			} else {

				request.setAttribute(Const.ATTRIB_KATALOGGRUPP, kg);
				request.setAttribute(Const.ATTRIB_KATALOGGRUPPLISTA, undergrupper);
				Integer tempGrpid = grpid;
				if (undergruppHittad) tempGrpid = underGrpid;
				ArrayList<Produkt> prod = SQLHandler.getProdukterInGrupp(Const.getConnection(request), tempGrpid, Const.getSessionData(request).getAvtalsKundnr(), true);
				if (prod!=null && prod.size()>0) {
					Produkt p = prod.get(1);
					Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:title\" content=\"");
					Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(kg.getRubrik()));
					Const.getInitData(request).addExtraHTMLHeaderContent("\">");
					Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:type\" content=\"article\">");
					Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image\" content=\"");
					Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p,250));
					Const.getInitData(request).addExtraHTMLHeaderContent("\">");
					Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image:secure_url\" content=\"");
					Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p,250));
					Const.getInitData(request).addExtraHTMLHeaderContent("\">");
					Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:description\" content=\"");
					Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(kg.getText()));
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
				request.getRequestDispatcher("/WEB-INF/outlet-header.jsp").include(request, response);				

				if (prod!=null && prod.size()>0) { 
					request.getRequestDispatcher("/WEB-INF/kbl-header.jsp").include(request, response);				
					for (Produkt p : prod) {
						request.setAttribute(Const.ATTRIB_PRODUKT, p);
						request.getRequestDispatcher("/WEB-INF/kbb-block-content.jsp").include(request, response);				
					}
					request.getRequestDispatcher("/WEB-INF/kbl-footer.jsp").include(request, response);				
				}
			}

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			
			Const.loggaSidvisning(request, "katalog", logID);
			
			
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
