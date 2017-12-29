<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="java.sql.SQLException"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.KatalogGruppLista"%>
<% Language lang = StartupData.getLanguage(); %>
<%
    KatalogGruppLista kgl=null;
    try {
        kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
    } catch (SQLException e) {}
    KatalogGrupp requestedGrupp = (KatalogGrupp)request.getAttribute(Const.ATTRIB_KATALOGREQUESTEDGRUPP); 
%>
            <div class="card trad">
                <% 
                    if (kgl!=null && kgl.isAvdelningar()) {
                %>
                    <ul>
                    <li class="trad-ind0"><%= lang.Avdelningar() %></li>
                    <% for (KatalogGrupp kg : kgl.getAvdelningar()) { %>
                    <li class="trad-ind1"><a href="<%= request.getContextPath() + "/katalog/" + kg.getGrpId() %>"><%= Const.toHtml(kg.getRubrik()) %></a></li>   
                    <% } %>
                    </ul>
                   
                <%    } %>
                <ul>
                    <% if (requestedGrupp!=null) for (KatalogGrupp kg : kgl.getGrupper().values()){ %>
                    <% if(requestedGrupp!=null && !requestedGrupp.getAvdelning().equals(kg.getAvdelning())) continue ; %>
                        <% if (kg.getDepth() <= Const.getStartupData().getKatalogTradMaxDisplayDepthLevel()) { %>
                            <li class="<%= kg.getDepth() > 0 ? (kg.getDepth() > 1 ? "trad-ind2" : "trad-ind1") : "trad-ind0" %>">
                                <% long id=Const.getInitData(request).getNewUniktID(); %>
                                <a id="trada<%= id %>" href="<%= request.getContextPath() +"/katalog/" + kg.getGrpId() %>" onclick="ajxCont(event, 'trada<%= id %>')"><%= Const.toHtml(kg.getRubrik()) %></a>
                            </li>
                        <% } %>
                    <% } %>
                </ul>
            </div>
