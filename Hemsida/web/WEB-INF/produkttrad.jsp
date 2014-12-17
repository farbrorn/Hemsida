<%@page import="java.sql.SQLException"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.KatalogGruppLista"%>
<%
    KatalogGruppLista kgl=null;
    try {
        kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
    } catch (SQLException e) {}
    KatalogGrupp requestedGrupp = (KatalogGrupp)request.getAttribute(Const.ATTRIB_KATALOGREQUESTEDGRUPP); 
%>
            <div class="trad">
                <% 
                    if (kgl!=null && kgl.isAvdelningar()) {
                %>
                <div>Avdelningar</div>
                <% for (KatalogGrupp kg : kgl.getAvdelningar()) { %>
                <div><a href="<%= request.getContextPath() + "/katalog/" + kg.getGrpId() %>"><%= Const.toHtml(kg.getRubrik()) %></a></div>   
                <% } %>
                   
                <%    } %>
                <ul>
                    <% for (KatalogGrupp kg : kgl.getGrupper().values()){ %>
                    <% if(requestedGrupp!=null && !requestedGrupp.getAvdelning().equals(kg.getAvdelning())) continue ; %>
                        <% if (kg.getDepth() <= Const.getKatalogTradMaxDisplayDepthLevel()) { %>
                            <li class="<%= kg.getDepth() > 0 ? (kg.getDepth() > 1 ? "trad-ind2" : "trad-ind1") : "trad-ind0" %>">
                                <a href="<%= request.getContextPath() +"/katalog/" + kg.getGrpId() %>"><%= Const.toHtml(kg.getRubrik()) %></a>
                            </li>
                        <% } %>
                    <% } %>
                </ul>
            </div>
