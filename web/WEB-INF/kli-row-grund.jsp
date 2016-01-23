<%@page import="se.saljex.hemsida.ProduktGrund"%>
<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.Artikel"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%
    ProduktGrund p = (ProduktGrund)request.getAttribute(Const.ATTRIB_PRODUKT);
    long rowCn = Const.getInitData(request).getNewUniktID();
    long id=0;
        boolean inkMoms=Const.getSessionData(request).isInkMoms(request);

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
                            </div>
                        </div>
                    </div>
