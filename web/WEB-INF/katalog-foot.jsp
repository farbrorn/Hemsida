<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="java.sql.Connection"%>
<%@page import="se.saljex.hemsida.SQLHandler"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.KatalogGruppLista"%>
<%@page import="se.saljex.hemsida.KatalogHeaderInfo"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.Const"%>
<%
KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
KatalogHeaderInfo khInfo = (KatalogHeaderInfo)request.getAttribute(Const.ATTRIB_KATALOGHEADERINFO);
List<KatalogGrupp> kglChildren = khInfo.getChildren();
    ArrayList<Produkt> topProd;

long id=0;
Connection con = Const.getConnection(request);
KatalogGrupp kg = khInfo.getKatalogGrupp();
%>
            <% if (!Const.isEmpty(kg.getHtmlHead())) { %>
                <div class="katalog-foot-beskrivning"><%= kg.getHtmlFoot() %></div>
            <% }  %>
