<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    SessionData sd = Const.getSessionData(request);
    boolean inkMoms=sd.isInkMoms(request);
        boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;
%>
                <div class="kid" itemscope itemtype="http://schema.org/Product">
                    <div class="kid-head">
                        <h2 itemprop="name"><%= Const.toHtml(p.getRubrik()) %></h2>
                        <span itemprop="description"><%= Const.toHtml(p.getKortBeskrivning()) %></span>
                    </div>
                    <div class="kid-img">
                        <img src="<%= Const.getArtStorBildURL(p) %>">
                        <meta itemprop="image" content="<%= Const.getArtFullBildURL(p) %>">
                    </div>
                    <div class="kid-order">
                        <div>
                            <div class="pris-stor" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
                                <% Artikel lagstaPrisArtikel = p.getLagstaPrisArtikel(isBruttopris); %>
                                Pris från <span class="kid-pris" itemprop="price" content="<%= lagstaPrisArtikel.getDisplayPris(inkMoms, isBruttopris) %>"><%= Const.getAnpassatPrisFormat(lagstaPrisArtikel.getDisplayPris(inkMoms, isBruttopris)) %> kr</span>/<%= Const.getFormatEnhet(lagstaPrisArtikel.getEnhet())%>
                            </div>
                        </div>
<% /*                        <div>
                            <div class="kop-antal">Antal:
                                <input maxlength="4" size="4">
                            </div>
                            <div class="kop-stor a-btn" onclick="javascript">KÖP</div>
                        </div>
   */ %>
                        <div class="kid-share">
                            <jsp:include page="/WEB-INF/share-buttons.jsp" />
                        </div>
                    </div>
                                <div class="kid-variant">Alla varianter  
                        <% int rowCn = 0; %>            
                        <% for (Artikel pv : p.getVarianter()) { %>
                            <div class="t-variant-row kid-variant-odd">
                                <div class="t-variant-namn"><%= pv.getKatNamn() %> 
                                    <span class="t-variant-namn-small"> Artnr: <span class="t-variant-artnr"><%= pv.getArtnr() %></span> 
                                    <% if(!Const.isEmpty(pv.getRsk())) { %>
                                        <br>RSK: <%= pv.getRsk() %>
                                    <% } %>
                                    </span>
                                </div>
                                <div class="t-variant-saldo"><%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr(),sd.isUserInloggad()) %>
                                </div>
                                
                                <div class="t-variant-pris-kop-hold">
                                    <% if (pv.getNettoPrisStaf2ExMoms()> 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf2(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf2(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf2()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                    <% } %>
                                    <% if (pv.getNettoPrisStaf1ExMoms()> 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf1(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf1(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf1()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                    <% } %>
                                    <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPris(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPris(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="1">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                </div>
                            </div>
                        <% } %>
                        <div class="momsinfo">
                            <% if (inkMoms) { %>
                                Priser inklusive moms.
                            <% } else { %>
                                Priser exklusive moms.
                            <% } %>
                            Preliminära lagersaldon för <%= Const.getSessionData(request).getLagerNamn() %>.
                        </div>
                    </div>
                    <%
                    ArrayList<Produkt> tillbehor = (ArrayList<Produkt>)request.getAttribute(Const.ATTRIB_SAMKOPTA_PRODUKTER);
                    if (tillbehor!=null && tillbehor.size()>0) {    
                    %>
                    <h3>Relaterade produkter</h3>
                    <%
                    %>
                        <jsp:include page="/WEB-INF/kbl-header.jsp" />
                    <% 
                        for (Produkt pp : tillbehor) {
                            request.setAttribute(Const.ATTRIB_PRODUKT, pp);
                    %>
                       <jsp:include page="/WEB-INF/kbl-block-content.jsp" />
                    <%     } %>
                    <jsp:include page="/WEB-INF/kbl-footer.jsp" />
                   <% } %> 

                    <h3>Specifikationer</h3>
                    <div class="kid-info">
                        <%= p.getBeskrivningHTML() %>
                    </div>
                    
                    <%
                    ArrayList<Produkt> lProdukter = (ArrayList<Produkt>)request.getAttribute(Const.ATTRIB_LIKNANDE_PRODUKTER);
                    if (lProdukter!= null) {
                    %>
                    <h3>Liknande produkter</h3>
                        <jsp:include page="/WEB-INF/kbl-header.jsp" />
                    <% 
                        for (Produkt pp : lProdukter) {
                            request.setAttribute(Const.ATTRIB_PRODUKT, pp);
                    %>
                       <jsp:include page="/WEB-INF/kbl-block-content.jsp" />
                    <%     } %>
                    <jsp:include page="/WEB-INF/kbl-footer.jsp" />
                   <% } %> 
                </div>
