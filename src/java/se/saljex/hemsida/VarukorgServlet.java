/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
                                
			} else if (Const.PARAM_VARUKORG_AC_EMPTY.equals(action )) {
				try {
                                    vk.clearVarukorgRows(Const.getConnection(request), Const.getSessionData(request).getInloggadKontaktId());
				} catch (SQLException e) {e.printStackTrace();}	
                                response.sendRedirect("/varukorg");
                                return;
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
					SendMail sm = new SendMail(mailsxmail);
                                        String orderMail;
                                        orderMail = Const.getStartupData().getLagerEnhetList().get(vkfHandler.getLagernr()).getEpost();
                                        if (orderMail==null || orderMail.length() < 5) orderMail = StartupData.getSxServOrderMail();

                                        if (StartupData.isHemsidaTestlage()) {
						sm.sendSimpleMail("ulf.hemma@gmail.com", "Inkommande testorder " + StartupData.getForetagNamn(), responseContent);
                                        } else if ("true".equals(Const.getStartupData().getConfig("Hemsida-AutosparaOrder", "false")) && sd.isUserInloggad()) {
                                                ArrayList<String> aArtnr = new ArrayList<>();
                                                ArrayList<Double> aAntal = new ArrayList<>();
                                                ArrayList<Double> aPris = new ArrayList<>();
                                                for (VarukorgProdukt vkProdukt : vkfHandler.getRows()) {
                                                    for (VarukorgArtikel a : vkProdukt.getVarukorgArtiklar()) {
                                                        aArtnr.add(a.getArtnr());
                                                        aAntal.add(a.getAntal()*a.getArt().getAntalSaljpack());
                                                        aPris.add(a.getArt().getNettoprisVidAntalSaljpack(a.getAntal(), false, false)/a.getArt().getAntalSaljpackForDivision());
                                                    }
                                                }
//create or replace function  orderAdd(in_anvandare varchar, in_lagernr integer, in_kundnr varchar, in_marke varchar, in_artnr varchar[], in_antal real[], in_pris real[], in_rab real[])
//returns integer as $$
                                                Connection con = Const.getConnection(request);
                                                
                                                PreparedStatement ps  = Const.getConnection(request).prepareStatement("select orderAdd(?,?,?,?,?,?,?,?)");
                                                ps.setString(1, "00");
                                                ps.setInt(2, vkfHandler.getLagernr());
                                                ps.setString(3, vkfHandler.getKundnr());
                                                ps.setString(4, vkfHandler.getMarke());
                                                ps.setArray(5, con.createArrayOf("varchar", aArtnr.toArray()));
                                                ps.setArray(6, con.createArrayOf("float4", aAntal.toArray()));
                                                ps.setArray(7, con.createArrayOf("float4", aPris.toArray()));
                                                ps.setObject(8, null);
                                                ResultSet rs = ps.executeQuery();
                                                Integer savedOrderNr = null;
                                                if (rs.next()) savedOrderNr = rs.getInt(1); else throw(new SQLException("Kan inte spara order. Får inget ordernummer från server.")); 
						sm.sendSimpleMail(orderMail, "Automatisk order från "  + StartupData.getForetagNamn() + " webbutik " + (new Date()).getTime(), "Order nr " + savedOrderNr + " är mottagen från webbutiken. ");
					} else {
						sm.sendSimpleMail(orderMail, "Inkommande order från "  + StartupData.getForetagNamn() + " webbutik " + (new Date()).getTime(), responseContent);
						if (!orderMail.equals(StartupData.getSxServOrderMail())) {
							sm.sendSimpleMail(StartupData.getSxServOrderMail(), "*KOPIA* på webborder sickad till annat lager "  + StartupData.getForetagNamn() + " webbutik " + (new Date()).getTime(), responseContent);
						}
					}

					try {
						//Skicka orderbekräftelse
						responseWrapper = getResponseWrapper(response);
						request.getRequestDispatcher("/WEB-INF/varukorg-confirm.jsp").include(request, responseWrapper);
						confirmResponseContent = responseWrapper.toString();
						sm = new SendMail( mailsxmail);
						sm.sendSimpleMail(vkfHandler.getEpost(), StartupData.getLanguage().Orderbekraftelse() + StartupData.getForetagNamn() + " ´" + StartupData.getLanguage().webborder(), confirmResponseContent);
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
		//Sätt session timeout så att den blir längre när det är fler rader i varukorgen
		if (vk!=null) {
			int tempRader = vk.getVarukorgProdukter().size();
			if (tempRader > 8) tempRader=8;
			if (tempRader <= 2) tempRader=1;
			request.getSession().setMaxInactiveInterval(tempRader*60*60*3);
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
