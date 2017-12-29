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
