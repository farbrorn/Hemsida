<%-- 
    Document   : varukorg-full
    Created on : 2014-nov-30, 09:47:46
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.VarukorgArtikel"%>
<%@page import="se.saljex.hemsida.VarukorgProdukt"%>
<%@page import="se.saljex.hemsida.VarukorgRow"%>
<%@page import="se.saljex.hemsida.Varukorg"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%
    SessionData sd = Const.getSessionData(request);
    Varukorg vk = sd.getVarukorg(Const.getConnection(request));
    long rowCn = Const.getInitData(request).getNewUniktID();
    
%>
    <% VarukorgProdukt vkProdukt; %>
    <% for (int cn = vk.getVarukorgProdukter().size()-1; cn >= 0; cn--) { %>
        <% vkProdukt = vk.getVarukorgProdukter().get(cn); %>
                    <div class="vk-row">
                        <div class="vk-img">
                            
                            <img src="<%= Const.getArtBildURL(vkProdukt.getVarukorgArtiklar().get(0).getArtnr()) %>">
                        </div>
                        <div class="vk-col">
                            <div class="vk-kl">
                                <div class="vk-kl-namn"><%= Const.toHtml(vkProdukt.getProdukt().getRubrik()) %></div>

                            </div>
                            <% for (VarukorgArtikel a : vkProdukt.getVarukorgArtiklar()) { %>
                                <% rowCn = Const.getInitData(request).getNewUniktID(); %>
                                <div class="vk-art">
                                    <div class="vk-variant"><%= Const.toHtml(a.getArt().getKatNamn()) %></div>
                                    <div class="vk-antal-enh">
                                        <div class="vk-antal"><%= a.getAntal() %> </div>
                                        <div class="vk-enh">
                                            <%= a.getArt().getAntalSaljpack().equals(1.0) ? a.getFormatEnhet() : "x " + Const.getAnpassade2Decimaler(a.getArt().getAntalSaljpack()) + a.getFormatEnhet() %> 
                                        </div>
                                    </div>
                                    <div class="vk-change">
                                        <input size="4" id="vk-antalinput-<%= rowCn %>" value="<%= a.getAntal() %>"/>
                                        <div class="vk-andra-btn vk-btn" onclick="vk_set(<%= vkProdukt.getProdukt().getKlasid() %>,'<%= a.getArt().getArtnr() %>','vk-antalinput-<%= rowCn %>');">Ä</div>
                                        <div class="vk-radera-btn vk-btn" onclick="vk_del(<%= vkProdukt.getProdukt().getKlasid() %>,'<%= a.getArt().getArtnr() %>');">X</div>
                                    </div>
                                </div>
                            <% } %>        
                        </div>
                          
                    </div>
    <% } %>
