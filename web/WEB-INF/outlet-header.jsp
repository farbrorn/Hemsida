<%-- 
    Document   : outlet-header
    Created on : 2017-jan-02, 22:15:15
    Author     : Ulf
--%>

<%@page import="java.util.List"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="se.saljex.hemsida.KatalogHeaderInfo"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Const"%>
<%
KatalogGrupp kg = (KatalogGrupp)request.getAttribute(Const.ATTRIB_KATALOGGRUPP);
List<KatalogGrupp> kgl = (List<KatalogGrupp>)request.getAttribute(Const.ATTRIB_KATALOGGRUPPLISTA);
%>

<div class="katalog-head" itemscope itemtype="http://schema.org/WebPage">
    <div class="katalog-head-sokvag">
        <h2><%= Const.toHtml(kg.getRubrik()) %></h2>
        <p itemprop="description"><%= Const.toHtml(kg.getText()) %> </p>
       <P />
        <meta itemprop="name" content="<%= StartupData.getDefaultHTMLTitle() + " - " + Const.toHtml(kg.getRubrik()) %>">

        <% if(kgl!=null && !kgl.isEmpty()) { %>
            <div class="katalog-head-sokvag-grp">
                <a href="<%= request.getContextPath() + "/outlet" %>"><%= Const.toHtml(kg.getRubrik() + " ") %></a>
            </div>
        
            <% for (KatalogGrupp g : kgl) { %>
            <div class="katalog-head-sokvag-grp">
                <a href="<%= request.getContextPath() + "/outlet/" + g.getGrpId() %>"><%= Const.toHtml(g.getRubrik() + " ") %></a>
            </div>
        <% } } %>
        
    </div>

</div>