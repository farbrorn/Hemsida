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
public class SetPropertyServlet extends HttpServlet {

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
			/* TODO output your page here. You may use following sample code. */
			String resp = "";
			SessionData sd = Const.getSessionData(request);
			
			//Lagernr
			Integer lagernr = null;
			try { lagernr = Integer.parseInt(request.getParameter(Const.PARAM_SETLAGERNR)); } catch (Exception e) {}
			if (lagernr!=null) {
				LagerEnhet lh = Const.getStartupData().getLagerEnhet(lagernr);
				if (lh!=null) {
					sd.setLager(request, lagernr);				
					resp = resp+"Lagernr=" + lagernr.toString();
					response.addCookie(Const.getInitData(request).getDataCookie().getCookie());
				}
			}
			
			//InkMoms
			String inkmomsStr = request.getParameter(Const.PARAM_SETINKMOMS);
			if (inkmomsStr!=null) {
				if ("true".equals(inkmomsStr.toLowerCase())) { sd.setInkMoms(request, true); resp=resp+"inkmoms=true";}
				if ("false".equals(inkmomsStr.toLowerCase())){ sd.setInkMoms(request, false); resp=resp+"inkmoms=false";}
			}
			
			out.println(resp);
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