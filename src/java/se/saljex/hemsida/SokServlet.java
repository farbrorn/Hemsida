/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 */
public class SokServlet extends HttpServlet {

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
		
		boolean contentOnly = Const.getInitData(request).isContentOnlyCall();
		
		try (PrintWriter out = response.getWriter()) {
			/* TODO output your page here. You may use following sample code. */
			String q=null;
			try {
				if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
				
				q= request.getParameter("q");
				SokResult sr = SQLHandler.sok(Const.getConnection(request), q, Const.getSessionData(request).getAvtalsKundnr(), Const.getSessionData(request).getLagerNr());
				if (sr!=null) {
					request.getRequestDispatcher("/WEB-INF/kli-header.jsp").include(request, response);				
					for (Produkt p : sr.getPl()) {
						request.setAttribute(Const.ATTRIB_PRODUKT, p);
						request.getRequestDispatcher("/WEB-INF/kli-row.jsp").include(request, response);				
					}
					request.getRequestDispatcher("/WEB-INF/kli-footer.jsp").include(request, response);				
				}
				if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				

 			} catch (SQLException e) { out.print("SQL-FEl"); e.printStackTrace(); }
			
			if (!contentOnly) Const.loggaSidvisning(request, "sok", q);
			
		}
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
