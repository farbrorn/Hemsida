<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    long rowCn = Const.getInitData(request).getNewUniktID();

 %>
<div class="kbl-block">
    <a href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                            <div class="kbl-t-img">
                                <img src="<%= Const.getArtBildURL(p) %>">
                            </div>
                            <div class="kbl-t-namn">
                                <h2><%= Const.toHtml(p.getRubrik()) %></h2>
                            </div>
                            <div class="kbl-t-besk">
                                <%= Const.toHtml(p.getKortBeskrivning()) %>
                            </div>
    </a>
                            <div class="kbl-t-variant">
                                <select id="variant-<%= rowCn %>" onchange="updateVariant(<%= rowCn %>)">
                                    <% 
                                    boolean firstRun=true;
                                    for (Artikel pv : p.getVarianter()) { %>
                                        <option aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris() %>" frp="<%= pv.getAntalSaljpack() %>" ilager="<%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %>" <%= firstRun ? "selected" : "" %>><%= Const.toHtml(pv.getKatNamn()) %></option>
                                        
                                    <%
                                        firstRun = false;
                                    } %>
                                </select>
                            </div>
                            <div class="kbl-t-pris">
                                <span class="kbl-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(p.getVarianter().get(0).getNettoPris()) %></span><span class="kbl-t-pris-per">/m</span>
                                <div class="kbl-t-ilager" id="ilager-<%= rowCn %>"><%= p.getVarianter().get(0).getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %></div>
                            </div>
                            <div class="kbl-t-antal-kop">
                                <div class="kbl-t-antal">Antal:
                                    <input size="4" id="antalinput-<%= rowCn %>">
                                </div>
                                <div class="kbl-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp
                                </div>
                            </div>
                        </div>
