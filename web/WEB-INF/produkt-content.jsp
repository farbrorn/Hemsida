<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="se.saljex.hemsida.SQLHandler"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<% Language lang = StartupData.getLanguage(); %>
<%
    Connection con = Const.getConnection(request);
    ResultSet rs;
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
    ArrayList<KatalogGrupp> katGrupper = SQLHandler.getGrupperForProduktEndastRubrikOchID(con,p.getKlasid());
    SessionData sd = Const.getSessionData(request);
    boolean inkMoms=sd.isInkMoms(request);
        boolean isBruttopris = Const.getSessionData(request).isUserInloggad() ? Const.getSessionData(request).isVisaBruttopris(request) : false;
%>
                <div class="kid" itemscope itemtype="http://schema.org/Product">
                    <div class="kid-head">
                        <div>
                            <%
                               if (katGrupper != null) for (KatalogGrupp kg : katGrupper) { 
                                   %><a href="/katalog/<%= kg.getGrpId() %>"><%= SXUtil.toHtml(kg.getRubrik()) %></a> &nbsp;&nbsp;<%
                               }
                             %>
                        </div>
                        <h2 itemprop="name"><%= Const.toHtml(p.getRubrik()) %></h2>
                        <%
                            
                            KatalogGrupp beskrivningsGrupp = null;
                            if ( p.getWebBeskrivningFranGrpid()!= null && p.getWebBeskrivningFranGrpid()!=0) {
                               beskrivningsGrupp = SQLHandler.getGrupp(con, p.getWebBeskrivningFranGrpid());
                            }                    
                            %>
                            <span itemprop="description"><%= beskrivningsGrupp==null ? Const.toHtml(p.getKortBeskrivning()) : Const.toHtml(beskrivningsGrupp.getText()) %></span>
                    </div>
                    <div class="kid-img">
                        <img src="<%= Const.getArtStorBildURL(p) %>">
                        <meta itemprop="image" content="<%= Const.getArtMinSizeBildURL(p,250) %>">
                    </div>
                    <div class="kid-order">
                        <div>
                            <div class="pris-stor" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
                                <% Artikel lagstaPrisArtikel = p.getLagstaPrisArtikel(isBruttopris); %>
                                <%= lang.Pris() %> <% if (p.getVarianter().size()>1) { %> <%= lang.fran() %> <% } %> <span class="kid-pris" itemprop="price" content="<%= lagstaPrisArtikel.getDisplayPris(inkMoms, isBruttopris) %>"><%= Const.getAnpassatPrisFormat(lagstaPrisArtikel.getDisplayPris(inkMoms, isBruttopris)) %> <%= lang.ValutaNamn() %></span>/<%= Const.getFormatEnhet(lagstaPrisArtikel.getEnhet())%>
                                <% if (!isBruttopris &&  lagstaPrisArtikel.getBruttoPris().compareTo(lagstaPrisArtikel.getNettoPrisExMoms()) > 0 )  { %> <br><%= lang.Listpris() %> <%= Const.getAnpassatPrisFormat(lagstaPrisArtikel.getDisplayPris(inkMoms, true)) %> <%= lang.ValutaNamn() %> <% } %> 
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
                                <div class="kid-variant"><%= lang.AllaVarianter() %>  
                        <% int rowCn = 0; %>            
                        <% for (Artikel pv : p.getVarianter()) { %>
                            <div class="t-variant-row kid-variant-odd">
                                <div class="t-variant-namn"><%= pv.getKatNamn() %> 
                                    <span class="t-variant-namn-small"> <%= lang.Artnr() %>: <span class="t-variant-artnr"><%= pv.getArtnr() %></span> 
                                    <% if(!Const.isEmpty(pv.getRsk())) { %>
                                        <br><%= lang.RSK() %>: <%= pv.getRsk() %>
                                    <% } %>
                                    </span>
                                </div>
                                <div class="t-variant-saldo"><%= lang.translateString(pv.getLagerSaldoString(Const.getSessionData(request).getLagerNr(),sd.isUserInloggad())) %>
                                </div>
                                
                                <div class="t-variant-pris-kop-hold">
                                    <% if (pv.getNettoPrisStaf2ExMoms()> 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf2(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf2(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                            <div class="t-variant-antal"><%= lang.Antal() %>:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf2()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                            <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');"><%= lang.Kop() %></div>
                                        </div>
                                    <% } %>
                                    <% if (pv.getNettoPrisStaf1ExMoms()> 0.0) { %>
                                        <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPrisStaf1(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPrisStaf1(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                /<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></div>
                                            <div class="t-variant-antal"><%= lang.Antal() %>:
                                                <input disabled id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="<%= Const.getFormatNumber(Math.ceil(pv.getAntalStaf1()/pv.getAntalSaljpackForDivision()),0) %>">
                                            </div>
                                                <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');"><%= lang.Kop() %></div>
                                        </div>
                                    <% } %>
                                    <% rowCn++; %>
                                        <div id="variant-<%= rowCn %>" class="t-variant-pris-kop" aid="<%= pv.getArtnr() %>" pris="<%= pv.getDisplayPris(inkMoms, isBruttopris)%>" frp="<%= pv.getAntalSaljpack() %>">
                                            <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getDisplayPris(inkMoms, isBruttopris)*pv.getAntalSaljpack()) %>
                                                <span class="t-variant-pris-enhet">/<%= pv.getAntalSaljpack().equals(1.0) ? Const.getFormatEnhet(pv.getEnhet()) : Const.getAnpassade2Decimaler(pv.getAntalSaljpack()) + pv.getFormatEnhet()  %></span>
                                                <% if (!isBruttopris &&  pv.getBruttoPris().compareTo(pv.getNettoPrisExMoms()) > 0 )  { %> 
                                                    <div class="t-variant-pris-listpris"><%= lang.Listpris() %>: <%= Const.getAnpassatPrisFormat(pv.getDisplayPris(inkMoms, true)*pv.getAntalSaljpack()) %> 
                                                    </div>
                                                <% } %>
                                            </div>
                                            <div class="t-variant-antal"><%= lang.Antal() %>:
                                                <input id="antalinput-<%= rowCn %>" size="4" maxlength="4" value="1">
                                            </div>
                                                <div class="t-variant-kop a-btn" onclick="vk_add(<%= p.getKlasid() %>,'variant-<%= rowCn %>','antalinput-<%= rowCn %>');"><%= lang.Kop() %></div>
                                        </div>
                                </div>
                            </div>
                        <% } %>
                        <div class="momsinfo">
                            <% if (inkMoms) { %>
                                <%= lang.PriserInklusiveMoms() %>
                            <% } else { %>
                                <%= lang.PriserExklusiveMoms() %>
                            <% } %>
                            <%= lang.PreliminaraLagersaldonFor() %> <%= Const.getSessionData(request).getLagerNamn() %>.
                        </div>
                    </div>
                    <%
                    ArrayList<Produkt> tillbehor = (ArrayList<Produkt>)request.getAttribute(Const.ATTRIB_SAMKOPTA_PRODUKTER);
                    if (tillbehor!=null && tillbehor.size()>0) {    
                    %>
                    <h3><%= lang.RelateradeProdukter() %></h3>
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

                    <h3><%= lang.Specifikationer() %></h3>
                    <div class="kid-info">
                        <%= beskrivningsGrupp==null ? p.getBeskrivningHTML() : beskrivningsGrupp.getHtmlHead() + beskrivningsGrupp.getHtmlFoot() %>
                    </div>
                    
                    <%
                    ArrayList<Produkt> lProdukter = (ArrayList<Produkt>)request.getAttribute(Const.ATTRIB_LIKNANDE_PRODUKTER);
                    if (lProdukter!= null) {
                    %>
                    <h3><%= lang.LiknandeProdukter() %></h3>
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
