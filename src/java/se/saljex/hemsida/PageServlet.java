/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.saljex.sxlibrary.SXUtil;

/**
 *
 * @author Ulf
 */
public class PageServlet extends HttpServlet {

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
		Connection con = Const.getConnection(request);
                se.saljex.loginservice.User adminUser = Const.getInitData(request).getAdminUser();
		try (PrintWriter out = response.getWriter()) {
			PageHandler pageHandler = new PageHandler(request, response);
			String sid=null;
			try {
				sid=request.getPathInfo();
				if (sid!=null) {
					pageHandler.loadAndParsePage(sid);
				}
			} catch (SQLException e) {
				Const.log("sql-fel i pageservlet " + e.toString());
				e.printStackTrace();
			}
			
			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:title\" content=\"");
			Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgTitle()!=null ? pageHandler.getOgTitle() : pageHandler.getRubrik()));
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:type\" content=\"");
			Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgType()!=null ? pageHandler.getOgTitle() : "article"));
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");

			if (!SXUtil.isEmpty(pageHandler.getOgImage())) {
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgImage()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image:secure_url\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgImage()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			}
			if (!SXUtil.isEmpty(pageHandler.getOgDescription())) {
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:description\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgDescription()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			}
			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:url\" content=\"");
			StringBuffer reqURL = request.getRequestURL();
			String qString=request.getQueryString();
			if (qString!=null) {
				reqURL.append("?");
				reqURL.append(qString);
			}
			Const.getInitData(request).addExtraHTMLHeaderContent(reqURL.toString()); 
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");


			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);

			if (!pageHandler.isPageParsed()) {
				Const.getInitData(request).setMetaRobotsNoIndex(true);
				request.getRequestDispatcher("/WEB-INF/pagenotfound.jsp").include(request, response);
			} else {
				out.print("<div class=\"sid\">");
				out.print(pageHandler.getParsedHTML());
                                if (adminUser!=null && adminUser.isBehorighet("WebAdmin")) 
                                    out.print("<a href=\"/Admin/EditPage" + sid  + "\" target=\"__blank\">Redigera sidan</a>");
				out.print("</div>");
			}
			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			Const.loggaSidvisning(request, "page", sid);
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
