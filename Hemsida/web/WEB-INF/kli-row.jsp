<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
 %>
                    <div>
                        <div class="kli-t-row kli-odd">
                            <a href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                                <div class="kli-t-img">
                                    <img src="<%= Const.getArtBildURL(p) %>">
                                </div>
                            </a>
                            <div class="kli-t-text">
                                <a href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                                    <div class="kli-t-namn-besk">
                                        <div class="kli-t-namn">
                                            <h2><%= Const.toHtml(p.getRubrik()) %></h2>
                                        </div>
                                        <div class="kli-t-besk"><%= Const.toHtml(p.getKortBeskrivning()) %></div>
                                    </div>
                                </a>
                                <div class="kli-t-variant-kop">
                                    <div class="kli-t-variant">
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
                                    <div class="kli-t-pris-kop">
                                        <div class="kli-t-pris">
                                             <span class="kli-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(p.getVarianter().get(0).getNettoPris()) %></span>
                                             <span class="kli-t-pris-per">/<%= Const.getFormatEnhet(p.getVarianter().get(0).getEnhet()) %></span>
                                             <div class="kli-t-ilager" id="ilager-<%= rowCn %>"><%= p.getVarianter().get(0).getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %></div>
                                        </div>
                                        <div class="kli-t-antal">Antal:
                                            <input size="4" id="antalinput-<%= rowCn %>">
                                        </div>
                                        <div class="kli-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
