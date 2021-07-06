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
        String action = request.getParameter(Const.PARAM_ACTION);
        Integer kid = null;
        try { kid = new Integer(request.getParameter(Const.PARAM_KLASID)); } catch (Exception e) {}
        String artnr = request.getParameter(Const.PARAM_ARTNR);
        Integer antal = null;

        response.setContentType("text/html;charset=UTF-8");
        
        boolean contentOnly = Const.getInitData(request).isContentOnlyCall();
        try (PrintWriter out = response.getWriter()) {
            SessionData sd = Const.getSessionData(request);
            Connection con = Const.getConnection(request);
            PreparedStatement ps;
            ResultSet rs;
            System.out.println("h1" + action);
            if (Const.PARAM_VARUKORG_AC_ADD.equals(action)) {
                if (sd.isUserInloggad() && artnr != null) {
                    try {
                        ps = con.prepareStatement("insert into vkorg (kontaktid, typ, klasid, artnr, antal) values (?,?,?,?,?) on conflict do nothing");
                        ps.setInt(1,sd.getInloggadKontaktId());
                        ps.setString(2, "FAV");
                        ps.setInt(3, 0);
                        ps.setString(4, artnr);
                        ps.setInt(5, 1);
                        ps.executeUpdate();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            } else if (Const.PARAM_VARUKORG_AC_REMOVE.equals(action)) {
                if (sd.isUserInloggad() && artnr != null) {
                    try {
                        ps = con.prepareStatement("delete from vkorg where kontaktid=? and typ=? and artnr = ?");
                        ps.setInt(1,sd.getInloggadKontaktId());
                        ps.setString(2, "FAV");
                        ps.setString(3, artnr);
                        ps.executeUpdate();
                    } catch (SQLException e) { e.printStackTrace(); }
                }

            } else if (!sd.isUserInloggad()) {
                if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
                out.print("Du måste vara inloggad för att komma åt dina listor.");
                if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);
            } else {
                if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-header.jsp").include(request, response);
                int rowCn=0;
                try {
                    ps = con.prepareStatement("select 1 as favtyp, case when coalesce(o1.marke,'')<>'' then o1.marke else o1.offertnr::varchar end as marke, o2.artnr, ceiling(o2.best / case when coalesce(a.minsaljpack,0) = 0 then 1 else a.minsaljpack end) as best  \n" +
                        "from offert1 o1 join offert2 o2 on o1.offertnr=o2.offertnr and o2.artnr not like 'Z%' join artikel a on a.nummer=o2.artnr\n" +
                        "where o1.kundnr=? and o1.offertnr in (select offertnr from offert2 where artnr='ZZ-WWW-001')\n" +
                        "\n" +
                        "union all\n" +
                        "select 0, 'Mina Favoriter', artnr, 1 from vkorg\n" +
                        "where typ='FAV'  and kontaktid=?\n" +
                        "\n" +
                        "order by 1, 2, 3");
                    ps.setString(1, sd.getInloggadKundnr());
                    ps.setInt(2, sd.getInloggadKontaktId());
                    rs = ps.executeQuery();
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
                                if (rs.getInt("favtyp") == 0) {
                                    out.print(SXUtil.toHtml(StartupData.getLanguage().MinaFavoriter()));
                                } else {
                                    out.print(SXUtil.toHtml(rs.getString("marke")));
                                }
                                out.print("<a class=\"lst-grupp-header-allbtn\" onclick=\"" +
                                        " var i; var x = document.getElementsByName('btn_list" + listCn + "'); " +
                                        " for (i = 0; i < x.length; i++) {" +
                                            "if (typeof x[i].onclick == 'function') { " +
                                            "    x[i].onclick.apply(x[i]); " + 
                                            " } " +
                                        " } " +
                                    " \">" + StartupData.getLanguage().LaggListanIVarukorgen() +" </a>");
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
                                out.print("<br>Lager: " + vk.getArt().getLagerSaldoString(sd.getLagerNr(), true));
                            out.print("</div>");

                            out.print("<div class=\"lst-row-kop\">");
                                out.print("<div name=\"btn_list" + listCn +"\" class=\"t-variant-kop a-btn lst-kop-btn\" onclick=\"vk_add(" + vk.getP().getKlasid() + ", 'variant-" + rowCn + "','antalinput-" + rowCn + "'); this.innerHTML='&check;';\">" + StartupData.getLanguage().Kop() +"</div>");
                            out.print("</div>");

                            if (rs.getInt("favtyp") == 0) {
                                out.print("<div class=\"lst-row-del\" onclick=\"fav_del('" + vk.getArt().getArtnr() + "'); location.reload();\">");
                                    out.print("&cross;");
                                out.print("</div>");
                            }

                        out.println("</div>"); //variant
                        
                    }
                } catch (SQLException e) {
                    out.print("Oj... Något gick fel och vi vet inte vad det är.");
                    e.printStackTrace();
                } finally {
                        if( rowCn>0) {
                            out.print("</div>"); // lst grpp
                        } else {
                            out.print("Du har inga aktiva listor.");
                        }
                    out.print("</div>"); //lst
                    if (!contentOnly) request.getRequestDispatcher("/WEB-INF/site-footer.jsp").include(request, response);                    
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
