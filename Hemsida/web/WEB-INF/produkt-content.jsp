<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
%>
                <div class="kid" itemscope itemtype="http://schema.org/Product">
                    <div class="kid-head">
                        <h2 itemprop="name"><%= Const.toHtml(p.getRubrik()) %></h2>
                        <span itemprop="description"><%= Const.toHtml(p.getKortBeskrivning()) %></span>
                    </div>
                    <div class="kid-img">
                        <img itemprop="image" src="http://saljex.se/p/s200/<%= p.getVarianter().get(0).getArtnr()%>.png">
                    </div>
                    <div class="kid-order">
                        <div>
                            <div class="pris-stor" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
                                <span class="kid-pris" itemprop="price" content="<%= p.getVarianter().get(0).getNettoPris() %>"><%= Const.getAnpassatPrisFormat(p.getVarianter().get(0).getNettoPris()) %> kr</span>/<%= Const.getFormatEnhet(p.getVarianter().get(0).getEnhet())%>
                            </div>
                        </div>
                        <div>
                            <div class="kop-antal">Antal:
                                <input maxlength="4" size="4">
                            </div>
                            <div class="kop-stor a-btn" onclick="javascript">KÖP</div>
                        </div>
                        <div>
                            Listpris:
                        </div>
                    </div>
                                <div class="kid-variant">Alla varianter  <div class="kid-variant-lagerfor">(Visar lagersaldo för <%= Const.getSessionData(request).getLagerNr() %>)</div>
                        <% int rowCn = 0; %>            
                        <% for (Artikel pv : p.getVarianter()) { %>
                            <div class="t-variant-row kid-variant-odd">
                                <div class="t-variant-namn"><%= pv.getKatNamn() %></div>
                                <div class="t-variant-saldo"><%= pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr()) %>
                                </div>
                                
                                <div class="t-variant-pris-kop-hold">
                                    <% if (pv.getNettoPrisStaf2() > 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris()%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getNettoPrisStaf2()) %>/<%= Const.getFormatEnhet(pv.getEnhet()) %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf2()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                    <% } %>
                                    <% if (pv.getNettoPrisStaf1() > 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris()%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getNettoPrisStaf1()) %>/<%= Const.getFormatEnhet(pv.getEnhet()) %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf1()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                    <% } %>
                                    <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getNettoPris()%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getNettoPris()) %>/<%= Const.getFormatEnhet(pv.getEnhet()) %></div>
                                            <div class="t-variant-antal">Antal:
                                                <input id="antalinput-<%= rowCn %>" size="4" maxlength="4" >
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                        </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                    <h3>Tillbehör</h3>
                    <jsp:include page="/WEB-INF/kbl-header.jsp" />
                    <jsp:include page="/WEB-INF/kbl-block-content.jsp" />
                    <jsp:include page="/WEB-INF/kbl-footer.jsp" />

                    <h3>Specifikationer</h3>
                    <div class="kid-info">
                        <%= p.getBeskrivningHTML() %>
                    </div>
                    <h3>Liknande produkter</h3>
                    <%
                    ArrayList<Produkt> lProdukter = (ArrayList<Produkt>)request.getAttribute(Const.ATTRIB_LIKNANDE_PRODUKTER);
                    if (lProdukter!= null) {
                    %>
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
