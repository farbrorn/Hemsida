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
            <h2><%= Const.toHtml(khInfo.getKatalogGrupp().getRubrik()) %></h2>
            <meta itemprop="name" content="<%= StartupData.getDefaultHTMLTitle() + " - " + Const.toHtml(khInfo.getKatalogGrupp().getRubrik()) %>">
            <meta itemprop="description" content="">
            
        <% for (KatalogGrupp g : khInfo.getSokvag()) { %>
            <div class="katalog-head-sokvag-grp">
                <% id=Const.getInitData(request).getNewUniktID(); %>                
                <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() + "/katalog/" + g.getGrpId() %>"><%= Const.toHtml(g.getRubrik() + "->") %></a></div>
        <% } %>
    </div>
    
    
    <div class="katalog-index">
        <%
            boolean rootPrinted=false;
            Integer startLevel = null;
            for (KatalogGrupp kg : kgl.getGrupper().values()) {
                if (startLevel == null && kg.getGrpId() == khInfo.getKatalogGrupp().getGrpId()) {
                    startLevel = kg.getDepth();
                    continue;
                }
                if (startLevel!= null) {
                    if (kg.getDepth() <= startLevel) break;
                    if (kg.getDepth() > startLevel+3) continue; //Skipa för långa nästlingar
                    if (kg.getDepth() == startLevel+1) {
                        if (rootPrinted) { %> </div> <%  }
                        rootPrinted=true;
                        %><div class="katalog-index-root"><%
                    }
                    %><div class="katalog-index-row">
                        <% id=Const.getInitData(request).getNewUniktID(); %>
                        <a id="conthead<%= id %>" onclick="ajxCont(event, 'conthead<%= id %>')" href="<%= request.getContextPath() +"/katalog/" + kg.getGrpId() %>">
                        <% if (kg.getDepth()==startLevel+1) {%>
                            <%= "<h3>" + Const.toHtml(kg.getRubrik()) + "</h3>" %>
                            <div class="katalog-index-row-imgs" >
                                <% for (Produkt p : kg.getTopProdukter(con)) { %>
                                <div class="katalog-index-row-img" ><img src="<%= Const.getArtBildURL(p) %>"></div>
                            <% } %>
                            </div>
                        <% } else {%>
                            <% if ( kg.getDepth() > startLevel+2) { %>
                                <%= (kg.getDepth() > startLevel+2 ? "&nbsp;&nbsp;" :"") +  Const.toHtml(kg.getRubrik()) %>
                            <% } else { %>
                            
                            <% //try { Produkt p = kg.getTopProdukter(con).get(0); %>
                             <%//   <div class="katalog-index-row-img" ><img src="<%= Const.getArtBildURL(p) "></div> %>
                            <% //}catch (IndexOutOfBoundsException e) {} %>
                                
                               <%=   Const.toHtml(kg.getRubrik())          %>
                            <% } %>
                        
                        <% } %>
                        </a>
                        </div>
                        <%
                }
            }
            if (rootPrinted) { %> </div> <% }
        %>
    </div>
</div>