/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.saljex.hemsida.Const;
import se.saljex.hemsida.KatalogGrupp;
import se.saljex.hemsida.Produkt;
import se.saljex.hemsida.SQLHandler;
import se.saljex.loginservice.LoginServiceConstants;
import se.saljex.loginservice.User;

/**
 *
 * @author ulf
 */
public class EditProdukt extends HttpServlet {

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
			String klasidStr=null;
			klasidStr = request.getParameter("klasid");
			Integer klasid = null;

                        Produkt prod =null;
			try { klasid = Integer.parseInt(klasidStr); }catch (Exception e) {}
                        User adminUser = null;
                        try { adminUser  = (User)request.getSession().getAttribute(LoginServiceConstants.REQUEST_PARAMETER_SESSION_USER); } catch (Exception e) {}
                        if (adminUser!= null && adminUser.isBehorighet("WebAdmin")){
                            if (klasid!=null) {
                                    if ("save".equals(action)) {
                                            try {
                                                PreparedStatement ps = Const.getConnection(request).prepareStatement("update artklase set rubrik=?, text=?, html=? where klasid=?");
                                                ps.setString(1, request.getParameter("rubrik"));
                                                ps.setString(2, request.getParameter("text"));
                                                ps.setString(3, request.getParameter("html"));
                                                ps.setInt(4, klasid);
                                                if (ps.executeUpdate() < 1) {
                                                            out.print("Inget sparat<br>");
                                                } 

                                            } catch (SQLException e) {
                                                    out.print("SQL-FEL: " + e.toString());
                                            }
                                    } 

                                    try {
                                        prod = SQLHandler.getProdukt(Const.getConnection(request), klasid, Const.getSessionData(request).getInloggadKundnr());
                                            request.setAttribute("produkt", prod);
                                            request.getRequestDispatcher("/WEB-INF/Admin/editprodukt.jsp").include(request, response);				
                                    } catch (SQLException e) { 
                                            out.print("SQL-FEL: " + e.toString());
                                    }	
                            } else { 
                                    out.print("Produkt hittades inte"); 
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
