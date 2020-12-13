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
 * @author ulf
 */
public class ListorServlet extends HttpServlet {

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
            if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
            SessionData sd = Const.getSessionData(request);
            Connection con = Const.getConnection(request);
            if (sd.isUserInloggad()) {
                try {
                    PreparedStatement ps = con.prepareStatement("select case when coalesce(o1.marke,'')<>'' then o1.marke else o1.offertnr::varchar end as marke, o2.artnr, ceiling(o2.best / case when coalesce(a.minsaljpack,0) = 0 then 1 else a.minsaljpack end) as best  from offert1 o1 join offert2 o2 on o1.offertnr=o2.offertnr and o2.artnr not like 'Z%' join artikel a on a.nummer=o2.artnr\n" +
                        "where o1.kundnr=? and o1.offertnr in (select offertnr from offert2 where artnr='ZZ-WWW-001')\n" +
                        "order by o1.marke, o1.offertnr, o2.pos");
                    ps.setString(1, sd.getInloggadKundnr());
                    ResultSet rs = ps.executeQuery();
                    int rowCn=0;
                    int listCn=0;
                    String tempMarke = null;
                    out.print("<div class=\"lst\">");
                    while (rs.next()) {
                        rowCn++;
                        if (tempMarke== null || !tempMarke.equals(rs.getString("marke"))) {
                            if (tempMarke != null) out.print("</div>");
                            listCn++;
                            tempMarke = rs.getString("marke");
                            out.print("<div class=\"lst-grupp\">");
                                out.print("<div class=\"lst-grupp-header\">");
                                out.print(SXUtil.toHtml(rs.getString("marke")));
                                out.print("<button onclick=\"" +
                                        " var i; var x = document.getElementsByName('btn_list" + listCn + "'); " +
                                        " for (i = 0; i < x.length; i++) {" +
                                            "if (typeof x[i].onclick == 'function') { " +
                                            "    x[i].onclick.apply(x[i]); " + 
                                            " } " +
                                        " } " +
                                    " \"> Alla </button>");
                                out.print("</div>");
                        }
                        VarukorgRow vk = SQLHandler.getArtikelProdukt(con, rs.getString("artnr"), null, sd.getInloggadKundnr(), sd.getLagerNr());
                        vk.getArt().addLagerSaldoRow(SQLHandler.getLagerSaldo(con, vk.getArt().getArtnr(), sd.getLagerNr()));
                        out.print("<div id=\"variant-" + rowCn + "\" aid=\"" + vk.getArt().getArtnr() + "\" class=\"lst-row "+ (rowCn%2 == 0 ? "" : "lst-row-odd")  +"\">");
                            out.print("<div class=\"vk-img vkf-img lst-row-img\">");
                                out.print("<img src=\"");
                                out.print(Const.getArtBildURL(vk.getArt().getArtnr()));
                                out.print("\">");
                            out.print("</div>");
                            
                            out.print("<div class=\"lst-row-artnr\">");
                                out.print(SXUtil.toHtml(vk.getArt().getArtnr()));
                            out.print("</div>");
                            
                            out.print("<div class=\"lst-row-namn\">");
                                out.print(SXUtil.toHtml(vk.getP().getRubrik()));
                                out.print(" ");
                                out.print(SXUtil.toHtml(vk.getArt().getKatNamn()));
                            out.print("</div>");

                            out.print("<div class=\"lst-row-antal\">");
                                out.print("<input id=\"antalinput-" + rowCn + "\" size=\"4\" maxlength=\"4\" value=\"" + rs.getInt("best") + "\">");
                                out.print(" " + (vk.getArt().getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(vk.getArt().getEnhet()) : Const.getAnpassade2Decimaler(vk.getArt().getAntalSaljpack()) + vk.getArt().getFormatEnhet()));
                                out.print("<br>Saldo: " + vk.getArt().getLagerSaldoString(sd.getLagerNr(), true));
                            out.print("</div>");
                            out.print("<div class=\"lst-row-kop\">");
                                out.print("<div name=\"btn_list" + listCn +"\" class=\"t-variant-kop a-btn lst-kop-btn\" onclick=\"vk_add(" + vk.getP().getKlasid() + ", 'variant-" + rowCn + "','antalinput-" + rowCn + "'); this.innerHTML='&check;';\">" + StartupData.getLanguage().Kop() +"</div>");
                            out.print("</div>");
                        out.print("</div>");
                        
                    }
                        out.print("</div>");
                    out.print("</div>");
                    if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);
                } catch (SQLException e) {
                    out.print("Oj... Något gick fel och vi vet inte vad det är.");
                    e.printStackTrace();
                }
                
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
