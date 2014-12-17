<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    Produkt p = (Produkt)request.getAttribute(Const.ATTRIB_PRODUKT);
%>
                <div class="kid">
                    <div class="kid-head">
                        <h2><%= Const.toHtml(p.getRubrik()) %></h2>
                        <%= Const.toHtml(p.getKortBeskrivning()) %>
                    </div>
                    <div class="kid-img">
                        <img src="http://saljex.se/p/s200/<%= p.getVarianter().get(0).getArtnr()%>.png">
                    </div>
                    <div class="kid-order">
                        <div>
                            <div class="pris-stor">
                                <span class="kid-pris"><%= Const.getAnpassatPrisFormat(p.getVarianter().get(0).getPris()) %> kr</span>/<%= Const.getFormatEnhet(p.getVarianter().get(0).getEnhet())%>
                            </div>
                        </div>
                        <div>
                            <div class="kop-antal">Antal:
                                <input maxlength="4" size="4">
                            </div>
                            <div class="kop-stor a-btn" onclick="javascript">KÖP</div>
                        </div>

                    </div>
                    <div class="kid-variant">Alla varianter
                        <% for (Artikel pv : p.getVarianter()) { %>
                            <div class="t-variant-row kid-variant-odd">
                                <div class="t-variant-namn"><%= pv.getNamn() %></div>
                                <div class="t-variant-pris-kop">
                                    <div class="t-variant-pris"><%= Const.getAnpassatPrisFormat(pv.getPris()) %> kr/ <%= pv.getEnhet() %></div>
                                    <div class="t-variant-antal">Antal:
                                        <input size="4" maxlength="4">
                                    </div>
                                    <div class="t-variant-kop a-btn">Köp</div>
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
