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
%>
<div class="katalog-head" itemscope itemtype="http://schema.org/WebPage">
    <div class="katalog-head-nav">
        <div class="katalog-head-nav-prev">
            <% if (khInfo.getPrevGrp() != null) { %>
            <% id=Const.getInitData(request).getNewUniktID(); %>
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + khInfo.getPrevGrp().getGrpId() %>"><%= Const.toHtml("<< Föregåene sida") %></a>
            <% } %>
        </div>
        <div class="katalog-head-nav-index">
            <% id=Const.getInitData(request).getNewUniktID(); %>
            <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath()+"/katalog/index/" + khInfo.getKatalogGrupp().getAvdelning() %>">Index</a>
        </div>
        
        <div class="katalog-head-nav-next">
            <% if (khInfo.getNextGrp() != null) { %>
                <% id=Const.getInitData(request).getNewUniktID(); %>
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + khInfo.getNextGrp().getGrpId()%>"><%= Const.toHtml("Nästa sida >>") %></a>
            <% } %>
        </div>
    </div>
        
    <div class="katalog-head-sokvag">
        <% for (KatalogGrupp g : khInfo.getSokvag()) { %>
            <div class="katalog-head-sokvag-grp">
                <% id=Const.getInitData(request).getNewUniktID(); %>                
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + g.getGrpId() %>"><%= Const.toHtml(g.getRubrik() + "->") %></a></div>
        <% } %>
        
            <h2><%= Const.toHtml(khInfo.getKatalogGrupp().getRubrik()) %></h2>
            <meta itemprop="name" content="<%= StartupData.getDefaultHTMLTitle() + " - " + Const.toHtml(khInfo.getKatalogGrupp().getRubrik()) %>">
            <meta itemprop="description" content="">
            
    </div>
    
</div>