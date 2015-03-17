<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
        boolean inkMoms=Const.getSessionData(request).isInkMoms();
        Artikel pv = p.getVariant((String)request.getAttribute(Const.ATTRIB_AID));
        
        if (pv==null) {
 %>
    Information om produkten saknas
    <% } else { %>
            <div class="asl-row">
                <a href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                    <div class="asl-img">
                        <img src="<%= Const.getArtBildURL(p) %>">
                    </div>
                </a>
                <div class="asl-text">
                    <a href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                            <div class="asl-rubrik">
                                <%= Const.toHtml(p.getRubrik()) %>
                            </div>
                            <div class="asl-variant" aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris(inkMoms) %>" frpenhet="<%= pv.getEnhetStringMedForpackning() %>" frp="<%= pv.getAntalSaljpack() %>" ilager="<%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %>"  %>
                                <%= Const.toHtml(pv.getKatNamn()) %>
                            </div>
                            <div class="asl-artnr"><%= Const.toHtml(pv.getArtnr()) %></div>
                    </a>
                </div>
                <div class="asl-pris">
                    <span class="asl-pris-pris"><%= Const.getAnpassatPrisFormat(pv.getNettoPris(inkMoms)) %></span>
                    <span class="asl-pris-per">/<%= Const.getFormatEnhet(pv.getEnhet()) %></span>
                    <span><%= pv.getEnhetStringMedForpackning() %></span>
                </div>
            </div>
<% } %>