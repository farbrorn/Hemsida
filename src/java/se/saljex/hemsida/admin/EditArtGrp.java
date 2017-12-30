/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.saljex.hemsida.Const;
import se.saljex.hemsida.KatalogGrupp;
import se.saljex.hemsida.SQLHandler;
import se.saljex.loginservice.LoginServiceConstants;
import se.saljex.loginservice.User;

/**
 *
 * @author Ulf
 */
public class EditArtGrp extends HttpServlet {

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
			String action = request.getParameter("ac");
			String grpidStr=null;
			grpidStr = request.getParameter("grpid");
			Integer grpid = null;
			KatalogGrupp grp=null;
			try { grpid = Integer.parseInt(grpidStr); }catch (Exception e) {}
                        User adminUser = null;
                        try { adminUser  = (User)request.getSession().getAttribute(LoginServiceConstants.REQUEST_PARAMETER_SESSION_USER); } catch (Exception e) {}
                        if (adminUser!= null && adminUser.isBehorighet("WebAdmin")){
                            if (grpid!=null) {
                                    if ("save".equals(action)) {
                                            try {
                                                    Integer sortOrder = null;
                                                    try { sortOrder = Integer.parseInt(action); } catch (Exception e) {}
                                                    if (sortOrder==null) sortOrder=0;

                                                    if (!SQLHandler.saveGrupp(Const.getConnection(request), grpid, sortOrder, request.getParameter("rubrik"), request.getParameter("text"), request.getParameter("htmlhead"), request.getParameter("htmlfoot"), "true".equals(request.getParameter("visaundergrupper")))) {
                                                            out.print("Inget sparat<br>");
                                                    } else {
                                                            KatalogGrupp k = Const.getStartupData().getKatalogGruppLista().getGrupper().get(grpid);
                                                            k.setSortOrder(sortOrder);
                                                            k.setRubrik(request.getParameter("rubrik"));
                                                            k.setText(request.getParameter("text"));
                                                            k.setHtmlHead(request.getParameter("htmlhead"));
                                                            k.setHtmlFoot(request.getParameter("htmlfoot"));
                                                            k.setVisaundergrupper("true".equals(request.getParameter("visaundergrupper")));							
                                                    }


                                            } catch (SQLException e) {
                                                    out.print("SQL-FEL: " + e.toString());
                                            }
                                    } 

                                    try {
                                            grp = SQLHandler.getGrupp(Const.getConnection(request), grpid);
                                            request.setAttribute("kataloggrupp", grp);
                                            request.getRequestDispatcher("/WEB-INF/Admin/editkataloggrupp.jsp").include(request, response);				
                                    } catch (SQLException e) { 
                                            out.print("SQL-FEL: " + e.toString());
                                    }	
                            } else { 
                                    out.print("Gruppid hittades inte"); 
                            }
                        } else { 
                                out.print("ingen  behörighet"); 
                        }
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
