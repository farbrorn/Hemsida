<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<% Language lang = StartupData.getLanguage(); %>

<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    Boolean tvingaBruttopris = false;
    try {tvingaBruttopris = (Boolean)request.getAttribute(Const.ATTRIB_PRINTASLISTPRIS); }catch(Exception e) {}
    if (tvingaBruttopris==null) tvingaBruttopris=false;
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
        boolean inkMoms=Const.getSessionData(request).isInkMoms(request);
        boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;
        if (isBruttopris) tvingaBruttopris=true;
        if (tvingaBruttopris) isBruttopris = true;
        
 %>
 <div style="page-break-inside: avoid">
 <table class="kat-main-table">
 <tr>
     <td class="kat-td-bild">
        <img src="<%= Const.getArtBildURL(p) %>" onerror="this.style.display='none'">
     </td>
     <td class="kat-td-cont">
         
        <h2><%= Const.toHtml(p.getRubrik()) %></h2>
        <div class="kat-t-besk"><%= Const.toHtml(p.getKortBeskrivning()) %></div>
        <table class="kat-table-art">
            <tr>
                <th class="kat-a-td-artnr">Art.nr</td>
                <th class="kat-a-td-namn">Benämning</td>
                <th class="kat-a-td-pris"><%= isBruttopris ? lang.Listpris() : lang.Pris() %> <%= inkMoms ? lang.InklMoms() : lang.ExklMoms() %></td>
                <th class="kat-a-td-enhet">Enhet</td>
                <th class="kat-a-td-rsk">RSK</td>
            </tr>
            <%
            for (Artikel pv : p.getVarianter()) {
                rowCn = Const.getInitData(request).getNewUniktID();
            %>
            <tr>
            <span id="variant-<%= rowCn %>" style="display: none;" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPris(inkMoms, isBruttopris) %>" frp="<%= pv.getAntalSaljpack() %>" ilager="<%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %>"></option>
                <td class="kat-a-td-artnr"><%= SXUtil.toHtml(pv.getArtnr()) %></td>
                <td class="kat-a-td-namn"><%= Const.toHtml(pv.getKatNamn()) %></td>
                <td class="kat-a-td-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPris(inkMoms, isBruttopris)) %></td>
                <td class="kat-a-td-enhet"><%= pv.getEnhetStringMedForpackning() %></td>
                <td class="kat-a-td-rsk"><%= Const.toHtml(pv.getRsk()) %></td>
                <td class="kat-td-kop">
                            <div class="kli-t-antal" style="display: none;">Antal:
                            <input size="4" id="antalinput-<%= rowCn %>">
                        </div>
                        <div class="kli-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp
                        </div>

                </td>
            </tr>
            
            <% } %>
        </table> 
         </td>
 </tr> 
 </table>
 </div>