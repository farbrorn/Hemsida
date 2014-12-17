<%@page import="se.saljex.hemsida.InitData"%>
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
    </a>
                            <div class="kbl-t-variant">
                                <select id="variant-<%= rowCn %>" onchange="updateVariant(<%= rowCn %>)">
                                    <% 
                                    boolean firstRun=true;
                                    for (Artikel pv : p.getVarianter()) { %>
                                        <option aid="<%= pv.getArtnr() %>" pris="<%= pv.getPris() %>" frp="<%= pv.getAntalSaljpack() %>" <%= firstRun ? "selected" : "" %>><%= Const.toHtml(pv.getKatNamn()) %></option>
                                        
                                    <%
                                        firstRun = false;
                                    } %>
                                </select>
                            </div>
                            <div class="kbl-t-pris">
                                <span class="kbl-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(p.getVarianter().get(0).getPris()) %></span><span class="kbl-t-pris-per">/<%= Const.toHtml(p.getVarianter().get(0).getEnhet()) %></span>
                            </div>
                            <div class="kbl-t-antal-kop">
                                <div class="kbl-t-antal" style="">Antal:
                                    <input size="4" id="antalinput-<%= rowCn %>" value="<%= p.getVarianter().get(0).getAntalSaljPackIForpack() %>">
                                </div> 
                                <div class="kbl-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp
                                </div>
                            </div>
                        </div>
