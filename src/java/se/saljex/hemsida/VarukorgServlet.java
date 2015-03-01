/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

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
	
	@Resource(name="sxmail", mappedName="sxmail") private Session mailsxmail;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpServletResponseWrapper responseWrapper;
		String responseContent=null;
		String confirmResponseContent=null;
		VarukorgRow vkr;
		Varukorg vk;
		SessionData sd = Const.getSessionData(request);
		InitData initData = Const.getInitData(request);
		vk = sd.getVarukorg(request);
		String action = request.getParameter(Const.PARAM_VARUKORG_AC);
		String get = request.getParameter(Const.PARAM_VARUKORG_GET);
		Integer kid = null;
		try { kid = new Integer(request.getParameter(Const.PARAM_KLASID)); } catch (Exception e) {}
		String artnr = request.getParameter(Const.PARAM_ARTNR);
		Integer antal = null;
		try { antal = new Integer(request.getParameter(Const.PARAM_ANTAL)); } catch (Exception e) {}
		VarukorgFormHandler vkfHandler=null;
		
		
		response.setContentType("text/html;charset=UTF-8");
		boolean contentOnly = Const.getInitData(request).isContentOnlyCall() || Const.PARAM_VARUKORG_GET_AJAX.equals(get);
		try (PrintWriter out = response.getWriter()) {
			
			if (Const.PARAM_VARUKORG_AC_ADD.equals(action ) ) {
				if (kid != null && artnr != null) {
					if (antal==null) antal = 1;
					try {
						vk.addArtikel(request,  kid, artnr, antal);
					} catch (SQLException e) {e.printStackTrace();}
				}
			} else if (Const.PARAM_VARUKORG_AC_REMOVE.equals(action )) {
			} else if (Const.PARAM_VARUKORG_AC_SET.equals(action )) {
				if (kid != null && artnr != null && antal != null) {
					try {
						vk.setArtikel(request, kid, artnr, antal);
					} catch (SQLException e) {e.printStackTrace();}
				}
				
			} else if (Const.PARAM_VARUKORG_AC_SAVEORDER.equals(action )) {
				try {
					vkfHandler = new VarukorgFormHandler(request);
					if (!vkfHandler.hasFormError()) {
						vk.setVarukorg(Const.getConnection(request), request, vkfHandler.getRows(), Const.getSessionData(request).getInloggadKontaktId());
					}
				} catch (SQLException e) {e.printStackTrace();}	
			} else { //Visa vaurkorg
				vkfHandler=new VarukorgFormHandler(vk.getVarukorgProdukter());
				if (sd.getInloggadUser()!=null) {
					vkfHandler.setKundnr(sd.getInloggadKundnr());
					vkfHandler.setKontaktperson(sd.getInloggadUser().getKontaktNamn());
					vkfHandler.setForetag(sd.getInloggadUser().getKundNamn());
					vkfHandler.setEpost(sd.getInloggadUser().getKontaktMail());
				}
			}
			if(!Const.PARAM_VARUKORG_GET_AJAX.equals(get)) {
				initData.setHideVarukorg(true);
			}			
			if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
			
			request.setAttribute(VarukorgFormHandler.PARAMETER_NAME, vkfHandler);
			if(Const.PARAM_VARUKORG_GET_AJAX.equals(get)) {
				request.getRequestDispatcher("/WEB-INF/varukorg-small-content.jsp").include(request, response);				
			} else if (Const.PARAM_VARUKORG_AC_SAVEORDER.equals(action ) && !vkfHandler.hasFormError()) {
				
				
				//Skicka e-post
				boolean fatalError=false;
				try {
					responseWrapper = getResponseWrapper(response);
					//Skicka order till ordermottagning först
					request.getRequestDispatcher("/WEB-INF/varukorg-order-email-content.jsp").include(request, responseWrapper);
					responseContent = responseWrapper.toString(); 
					SendMail sm = new SendMail(Const.getConnection(request), mailsxmail);
					if (Const.isHemsidaTestlage(Const.getConnection(request))) {
						sm.sendSimpleMail(Const.getConnection(request), "ulf.hemma@gmail.com", "Inkommande testorder " + Const.getForetagNamn(), responseContent);
					} else {
						sm.sendSimpleMail(Const.getConnection(request), Const.getSxServOrderMail(Const.getConnection(request)), "Inkommande order från "  + Const.getForetagNamn() + " webbutik", responseContent);
					}

					try {
						//Skicka orderbekräftelse
						responseWrapper = getResponseWrapper(response);
						request.getRequestDispatcher("/WEB-INF/varukorg-confirm.jsp").include(request, responseWrapper);
						confirmResponseContent = responseWrapper.toString();
						sm = new SendMail(Const.getConnection(request), mailsxmail);
						sm.sendSimpleMail(Const.getConnection(request), vkfHandler.getEpost(), "Orderbekräftelse " + Const.getForetagNamn() + "webborder", confirmResponseContent);
					} catch (Exception e) {Const.log("Fel vid sändning av Orderbekräftelse E-mail. " + vkfHandler.getEpost() + " - " +responseContent); e.printStackTrace(); }
					
					
					
				}  catch (Exception e) { fatalError=true; Const.log("Fel vid sändning av Order E-mail. " + responseContent); e.printStackTrace();  }
			
				if (!fatalError) {
					out.print(confirmResponseContent);
					try {
						vk.clearVarukorgRows(Const.getConnection(request), Const.getSessionData(request).getInloggadKontaktId());
					} catch (SQLException e) { Const.log("Fel vid rensa varukorg"); e.printStackTrace();}
				}
				else {
					request.getRequestDispatcher("/WEB-INF/varukorg-save-error.jsp").include(request, response);
				}
			} else {
				request.getRequestDispatcher("/WEB-INF/varukorg-full.jsp").include(request, response);
			}
			if( !contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);				
			
			
		}		
	}

	private HttpServletResponseWrapper getResponseWrapper(HttpServletResponse response) {
		return new HttpServletResponseWrapper(response) {
            private final StringWriter sw = new StringWriter();

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(sw);
            }

            @Override
            public String toString() {
                return sw.toString();
            }
        };		
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
