<%@page import="se.saljex.hemsida.InitData"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
    boolean inkMoms=Const.getSessionData(request).isInkMoms();
    Artikel forvaldVariant = p.getVarianter().size() > 0 ? p.getVarianter().get(0) : null;
    if (forvaldVariant!=null) {
 %>
<div class="kbl-block">
    <% id=Const.getInitData(request).getNewUniktID(); %>
    <a id="contkbl<%= id %>" onclick="ajxCont(event, 'contkbl<%= id %>')" href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
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
                                        <option aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris(inkMoms)%>" frp="<%= pv.getAntalSaljpack() %>" ilager="<%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %>" <%= firstRun ? "selected" : "" %>><%= Const.toHtml(pv.getKatNamn()) %></option>
                                        
                                    <%
                                        firstRun = false;
                                    } %>
                                </select>
                            </div>
                            <div class="kbl-t-pris">
                                <span class="kbl-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(forvaldVariant.getNettoPris(inkMoms)) %></span><span class="kbl-t-pris-per">/<%= Const.getFormatEnhet(forvaldVariant.getEnhet()) %></span>
                                <div class="kbl-t-ilager" id="ilager-<%= rowCn %>"><%= forvaldVariant.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %></div>

                            </div>
                            <div class="kbl-t-antal-kop">
                                <div class="kbl-t-antal" style="">Antal:
                                    <input size="4" id="antalinput-<%= rowCn %>" value="<%= forvaldVariant.getAntalSaljPackIForpack() %>">
                                </div> 
                                <div class="kbl-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp
                                </div>
                            </div>
                        </div>
<% } %>