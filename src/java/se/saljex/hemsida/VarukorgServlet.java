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
public class VarukorgServlet extends HttpServlet {

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
			VarukorgRow vkr;
			Varukorg vk;
			SessionData sd = Const.getSessionData(request);
			vk = sd.getVarukorg(Const.getConnection(request));
			String action = request.getParameter(Const.PARAM_VARUKORG_AC);
			String get = request.getParameter(Const.PARAM_VARUKORG_GET);
			Integer kid = null;
			try { kid = new Integer(request.getParameter(Const.PARAM_KLASID)); } catch (Exception e) {}
			String artnr = request.getParameter(Const.PARAM_ARTNR);
			Integer antal = null;
			try { antal = new Integer(request.getParameter(Const.PARAM_ANTAL)); } catch (Exception e) {}
			
			if (Const.PARAM_VARUKORG_AC_ADD.equals(action ) ) {
				if (kid != null && artnr != null) {
					if (antal==null) antal = 1;
					try {
						vk.addArtikel(Const.getConnection(request), sd.getInloggadKontaktId(), kid, artnr, antal);
					} catch (SQLException e) {e.printStackTrace();}
				}
			} else if (Const.PARAM_VARUKORG_AC_REMOVE.equals(action )) {
			} else if (Const.PARAM_VARUKORG_AC_SET.equals(action )) {
				if (kid != null && artnr != null && antal != null) {
					try {
						vk.setArtikel(Const.getConnection(request), sd.getInloggadKontaktId(), kid, artnr, antal);
					} catch (SQLException e) {e.printStackTrace();}
				}
				
			}

//			request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);			
			if(Const.PARAM_VARUKORG_GET_AJAX.equals(Const.PARAM_VARUKORG_GET_AJAX)) {
				request.getRequestDispatcher("/WEB-INF/varukorg-small-content.jsp").include(request, response);				
			} else {
				request.getRequestDispatcher("/WEB-INF/varukorg-full.jsp").include(request, response);
			}
//			request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				

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
