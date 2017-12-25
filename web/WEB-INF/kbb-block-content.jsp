<%-- 
    Document   : kbb-block-content
    Created on : 2017-jan-02, 17:44:00
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.InitData"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="se.saljex.hemsida.LagerSaldo"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    if (p==null) return;
    InitData id = Const.getInitData(request);
    long rowCn = id.getNewUniktID();
    boolean inkMoms=Const.getSessionData(request).isInkMoms(request);
    boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;

 %>
<div class="kbb-block">
                            <div class="kbb-t-img">
                                <img src="<%= Const.getArtStorBildURL(p) %>">
                            </div>
                            <div class="kbb-t-namn">
                                <h2><%= Const.toHtml(p.getRubrik()) %></h2>
                            </div>
                            <div class="kbb-t-besk">
                                <%= Const.toHtml(p.getKortBeskrivning()) %>
                            </div>
                            <div class="kbb-t-variant">
                                    <% 
                                    boolean firstRun=true;
                                    boolean variantILagerHittad = false;
                                    for (Artikel pv : p.getVarianter()) { %>
                                        <% rowCn= id.getNewUniktID(); %>
                                        <% 
                                            String lagerSaldoStr = "";
                                            for (LagerSaldo ls : pv.getLagerSaldon().values()) {
                                                if (SXUtil.noNull(ls.getTillgangliga()).compareTo(0.0) > 0) {
                                                    if (lagerSaldoStr.length()>0) lagerSaldoStr += ", ";
                                                    lagerSaldoStr += ls.getLagernamn() + " (" + SXUtil.getFormatNumber(ls.getTillgangliga(),0) + " " + pv.getFormatEnhet() + ")";
                                                    variantILagerHittad=true;
                                                }
                                            }
                                            
                                        %>

                                        <% boolean finnsPaLager = lagerSaldoStr.length() > 0;  %>
                                        <div class="t-variant-row kid-variant-odd">
                                            <div class="t-variant-namn kbb-t-variant-namn"><%= pv.getKatNamn() %> 
                                                <span class="t-variant-namn-small kbb-t-variant-namn-small"> Artnr: <span class="t-variant-artnr"><%= pv.getArtnr() %></span> 
                                                </span>
                                            </div>


                                            <div class="t-variant-pris-kop-hold kbb-t-variant-pris-kop-hold">
                                                <% if (finnsPaLager && pv.getNettoPrisStaf2ExMoms()> 0.0) { %>
                                                    <% rowCn= id.getNewUniktID(); %>
                                                    <div id="variant-<%= rowCn %>" class="t-variant-pris-kop kbb-t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf2(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                                        <div class="t-variant-pris kbb-t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf2(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                            /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                                        <div class="t-variant-antal kbb-t-variant-antal">Antal:
                                                            <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf2()/pv.getAntalSaljpackForDivision()),0) %>">
                                                        </div>
                                                        <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                                    </div>
                                                <% } %>
                                                <% if (finnsPaLager && pv.getNettoPrisStaf1ExMoms()> 0.0) { %>
                                                    <% rowCn= id.getNewUniktID(); %>
                                                    <div id="variant-<%= rowCn %>" class="t-variant-pris-kop kbb-t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf1(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                                        <div class="t-variant-pris kbb-t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf1(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                            /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                                        <div class="t-variant-antal kbb-t-variant-antal">Antal:
                                                            <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf1()/pv.getAntalSaljpackForDivision()),0) %>">
                                                        </div>
                                                        <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');">Köp</div>
                                                    </div>
                                                <% } %>
                                                <% rowCn= id.getNewUniktID(); %>
                                                    <div id="variant-<%= rowCn %>" class="t-variant-pris-kop kbb-t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPris(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                                        <div class="t-variant-pris kbb-t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPris(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                            /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                                        <div class="t-variant-antal kbb-t-variant-antal" <%= finnsPaLager ? "" : "Style=\"visibility: hidden\"" %> >Antal:
                                                            <input id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="1">
                                                        </div>
                                                        <div class="t-variant-kop a-btn"
                                                        <% if (finnsPaLager) {%>
                                                            onclick="vk_add(<%=  p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');"
                                                         <% } else { %>
                                                            style="visibility: hidden"
                                                         <% } %>
                                                            >
                                                            Köp</div>
                                                    </div>
                                            </div>
                                            <div class="t-variant-saldo kbb-t-variant-saldo"><%= finnsPaLager ? "Finns på lager i " + SXUtil.toHtml(lagerSaldoStr) : "Slutsåld" %> </div>
                                        </div>

                                    
                                    
                                    <%
                                        firstRun = false;
                                    } %>
                                    
                            </div>
                            <div class="kbb-t-pris">
                                <% Artikel lagstaPrisArtikel = p.getLagstaPrisArtikel(isBruttopris); %>
                                <% if (p.getVarianter().size()>1) { %>Från <% } %>
                                <span class="kbb-t-pris-pris" id="pris-<%= rowCn %>"><%= Const.getAnpassatPrisFormat(lagstaPrisArtikel.getDisplayPris(inkMoms, isBruttopris)) %></span><span class="kbb-t-pris-per">/<%= Const.getFormatEnhet(lagstaPrisArtikel.getEnhet()) %></span>
                            </div>
                        </div>
