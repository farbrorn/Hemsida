/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 */
public class KontoServlet extends HttpServlet {

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
			Const.getInitData(request).setMetaRobotsNoIndex(true);

			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);

			if (Const.getSessionData(request).getInloggadUser()==null) {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
				request.getRequestDispatcher("/WEB-INF/konto-ejinloggad.jsp").include(request, response);
			} else {
				String sid=request.getPathInfo();
				if (sid==null) {
					
					request.getRequestDispatcher("/WEB-INF/konto-hem.jsp").include(request, response);
				} 
				else {
					Const.getInitData(request).setMetaRobotsNoIndex(true);
					request.getRequestDispatcher("/WEB-INF/pagenotfound.jsp").include(request, response);
				}
			
			}
			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				

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
