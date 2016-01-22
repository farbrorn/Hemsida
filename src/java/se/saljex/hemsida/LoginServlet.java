/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.annotation.Resource;
import javax.mail.Session;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import se.saljex.sxlibrary.SXUtil;

/**
 *
 * @author Ulf
 */
public class LoginServlet extends HttpServlet {
@Context ServletContext ctx;
	@Resource(name="sxmail", mappedName="sxmail") private Session mailsxmail;

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
			KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
			SessionData sd = Const.getSessionData(request);
			String namn = request.getParameter(Const.PARAM_LOGINNAMN);
			String losen = request.getParameter(Const.PARAM_LOGINLOSENL);
			String ac = request.getParameter(Const.PARAM_ACTION);
			Integer inloggadId = sd.getInloggadKontaktId();
			Const.getInitData(request).setMetaRobotsNoIndex(true);
			if (inloggadId!=null) {
				request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
				request.getRequestDispatcher("/WEB-INF/login-inloggad.jsp").include(request, response);												
				request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			} else {
				if (Const.ACTION_GLOMTLOSEN.equals(ac)) {
						request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
						request.getRequestDispatcher("/WEB-INF/login-glomtlosen.jsp").include(request, response);														
						request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
				} else if (Const.ACTION_SKICKANYTTLOSEN.equals(ac)) {
						String nyttLosen = SQLHandler.loginSkapaNyttLosen(Const.getConnection(request), namn);
						if (nyttLosen != null) {
							try {
								SendMail sm = new SendMail(mailsxmail);
								if (StartupData.isHemsidaTestlage()) {
									sm.sendSimpleMail("ulf.hemma@gmail.com", "Nytt lösenord" , "Här kommer ditt nya lösenord: " + nyttLosen);
								} else {
									sm.sendSimpleMail(StartupData.getSxServOrderMail(), "Nytt lösenord för "  + StartupData.getForetagNamn() + " webbutik", "Ditt nya lösenord är: " + nyttLosen);
								}
							} catch (Exception e) {}
						} else {
							//Gör inget särskilt. Vi vill inte avslöja att t.ex.användarnamnet var ogiltigt
						}
						request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
						request.getRequestDispatcher("/WEB-INF/login-glomtlosen-confirm.jsp").include(request, response);														
						request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
					
				} else if (namn!= null && losen!=null) {
					if (sd.login(Const.getConnection(request), request, namn, losen)==null) {
						request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
						request.getRequestDispatcher("/WEB-INF/login.jsp").include(request, response);														
						request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
					} else {
						sd.setAuoLogin(Const.getConnection(request), response);
						response.sendRedirect(request.getContextPath() + "/");							
						//request.getRequestDispatcher("/WEB-INF/login-inloggad.jsp").include(request, response);																
					}
				} else {
					request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
					request.getRequestDispatcher("/WEB-INF/login.jsp").include(request, response);								
					request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
				}
			}
		
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
