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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 */
public class ProduktServlet extends HttpServlet {

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

			Integer klasid = null;
			Produkt p = null;
			String[] pathArr = request.getPathInfo().split("/");
			if (pathArr!=null) {
				try { klasid = new Integer(pathArr[1]); } catch (Exception e) {} 
				if (klasid!=null) p = SQLHandler.getProdukt(Const.getConnection(request), klasid, Const.getSessionData(request).getAvtalsKundnr());
			}
			KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
//			request.setAttribute(Const.ATTRIB_KATALOGGRUPPLISTA, kgl);

			request.setAttribute(Const.ATTRIB_PRODUKT, p);
			
			if (p!=null && p.getAutoSamkoptaKlasarAsArray()!=null) {
				Produkt tempProdukt;
				ArrayList<Produkt> arrSamkopta = new ArrayList<>();
				for (Integer i : p.getAutoSamkoptaKlasarAsArray()) {
					tempProdukt = SQLHandler.getProdukt(Const.getConnection(request), i, Const.getSessionData(request).getAvtalsKundnr());
					if (tempProdukt!=null) arrSamkopta.add(tempProdukt);
				}
				request.setAttribute(Const.ATTRIB_SAMKOPTA_PRODUKTER, arrSamkopta);
				
				
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:title\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(p.getRubrik()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:type\" content=\"product\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image:secure_url\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.getArtMinSizeBildURL(p));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:description\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(p.getKortBeskrivning()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				
			}

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);

			if (p!=null) {
				request.getRequestDispatcher("/WEB-INF/produkt-content.jsp").include(request, response);
			} else {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
				request.getRequestDispatcher("/WEB-INF/produkt-ej-hittad.jsp").include(request, response);				
			}

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);
			Const.loggaSidvisning(request, "produkt", klasid);
		}
		catch (SQLException e) { e.printStackTrace(); throw new ServletException("SQL-Fel"); }
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
