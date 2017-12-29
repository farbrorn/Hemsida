<%@page import="se.saljex.hemsida.Language"%>
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
<% Language lang = StartupData.getLanguage(); %>
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
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + khInfo.getPrevGrp().getGrpId() %>"><%= Const.toHtml(lang.ForegaendeSida()) %></a>
            <% } %>
        </div>
        <div class="katalog-head-nav-index">
            <% id=Const.getInitData(request).getNewUniktID(); %>
            <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath()+"/katalog/index/" + khInfo.getKatalogGrupp().getAvdelning() %>"><%= lang.Index() %></a>
        </div>
        
        <div class="katalog-head-nav-next">
            <% if (khInfo.getNextGrp() != null) { %>
                <% id=Const.getInitData(request).getNewUniktID(); %>
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + khInfo.getNextGrp().getGrpId()%>"><%= Const.toHtml(lang.NastaSida()) %></a>
            <% } %>
        </div>
    </div>
        
    <div class="katalog-head-sokvag">
        <% for (KatalogGrupp g : khInfo.getSokvag()) { %>
            <div class="katalog-head-sokvag-grp">
                <% id=Const.getInitData(request).getNewUniktID(); %>                
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + g.getGrpId() %>"><%= Const.toHtml(g.getRubrik() + "->") %></a></div>
        <% } %>
        
            <% KatalogGrupp kg = khInfo.getKatalogGrupp(); %>
            <h2><%= Const.toHtml(kg.getRubrik()) %></h2>
            <meta itemprop="name" content="<%= StartupData.getDefaultHTMLTitle() + " - " + Const.toHtml(khInfo.getKatalogGrupp().getRubrik()) %>">
            <meta itemprop="description" content="">
            <% if (!Const.isEmpty(kg.getHtmlHead())) { %>
                <div class="katalog-head-beskrivning"><%= kg.getHtmlHead() %></div>
            <% } else if (!Const.isEmpty(kg.getText())) { %>
                <div class="katalog-head-beskrivning"><%= Const.toHtml(kg.getText()) %></div>
            <% }%>        
            
    </div>
    
</div>