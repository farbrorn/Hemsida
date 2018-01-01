<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.InitData"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<% Language lang = StartupData.getLanguage(); %>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    if (p==null) return;
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
    boolean inkMoms=Const.getSessionData(request).isInkMoms(request);
    boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;
    Artikel forvaldVariant = p.getVarianter().size() > 0 ? p.getVarianter().get(0) : null;
    if (forvaldVariant!=null) {
 %>
<div class="kbl-block">
    <% id=Const.getInitData(request).getNewUniktID(); %>
    <a id="contkbl<%= id %>" onclick="ajxCont(event, 'contkbl<%= id %>')" href="<%= request.getContextPath() + "/produkt/"+ p.getKlasid() %>">
                            <div class="kbl-t-img">
                                <img src="<%= Const.getArtBildURL(p) %>" onerror="this.style.display='none'">
                            </div>
                            <div class="kbl-t-namn">
                                <h2><%= Const.toHtml(p.getRubrik()) %></h2>
                            </div>
    </a>
                            <div class="kbl-t-variant">
                                <% boolean singleeVariant=false;
                                try { singleeVariant = p.getVarianter().size()==1; } catch(Exception e) {}
                                %>
                                <% if (singleeVariant) { %>
                                    <div class="kbl-t-variant-select"><%= Const.toHtml(p.getVarianter().get(0).getKatNamn()) %></div> 
                                <% } %>
                                <select class="kbl-t-variant-select" style="<%= singleeVariant ? "display:none;" : "" %>" id="variant-<%= rowCn %>" onchange="updateVariant(<%= rowCn %>)">
                                    <% 
                                    boolean firstRun=true;
                                    for (Artikel pv : p.getVarianter()) { %>
                                        <option aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPris(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>" ilager="<%= lang.translateString(pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr())) %>" <%= firstRun ? "selected" : "" %>><%= Const.toHtml(pv.getKatNamn()) %></option>
                                        
                                    <%
                                        firstRun = false;
                                    } %>
                                </select>
                            </div>
                            <div class="kbl-t-pris">
                                <span class="kbl-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(forvaldVariant.getDisplayPris(inkMoms, isBruttopris)) %></span><span class="kbl-t-pris-per">/<%= Const.getFormatEnhet(forvaldVariant.getEnhet()) %></span>
                                <div class="kbl-t-ilager" id="ilager-<%= rowCn %>"><%= lang.translateString(forvaldVariant.getLagerSaldoString(Const.getSessionData(request).getLagerNr())) %></div>

                            </div>
                            <div class="kbl-t-antal-kop">
                                <div class="kbl-t-antal" style=""><%= lang.Antal() %>:
                                    <input size="4" id="antalinput-<%= rowCn %>" value="<%= forvaldVariant.getAntalSaljPackIForpack() %>">
                                </div> 
                                <div class="kbl-t-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');"><%= lang.Kop() %>
                                </div>
                            </div>
                        </div>
<% } %>