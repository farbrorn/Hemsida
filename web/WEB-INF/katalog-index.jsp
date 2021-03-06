<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="java.sql.Connection"%>
<%@page import="se.saljex.hemsida.Produkt"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.SQLHandler"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page import="se.saljex.hemsida.KatalogGruppLista"%>
<%@page import="se.saljex.hemsida.Const"%>
<% Language lang = StartupData.getLanguage(); %>
<%
KatalogGruppLista kgl = Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request));
KatalogGrupp avdelning = (KatalogGrupp)request.getAttribute(Const.ATTRIB_KATALOGAVDELNING);
long id=0;
Connection con = Const.getConnection(request);

%>
<div class="katalog-index">
    <%
        boolean firstRun=true;
    int divBreakLevel = kgl.isAvdelningar() ? 1 : 0;

     if (kgl.isAvdelningar()) { 
        firstRun=true;
        for(KatalogGrupp kg : kgl.getAvdelningar()) { %>
            <div class="katalog-index-avd">
                <% id=Const.getInitData(request).getNewUniktID(); %>
                <a id="contind<%= id %>" onclick="ajxCont(event, 'contind<%= id %>')" href="<%= request.getContextPath() + "/katalog/index/" + kg.getGrpId() %>"><%= Const.toHtml(kg.getRubrik()) %></a></div>
        <% 
            firstRun=false;
        }
    } 
    
    firstRun=true;
    ArrayList<Produkt> topProd;
    for (KatalogGrupp  kg : kgl.getGrupper().values()) { 
    if (avdelning!=null && !avdelning.getGrpId().equals(kg.getAvdelning())) continue; 
        if (!firstRun && kg.getDepth() == divBreakLevel) { %>
            </div>
        <%    }
        if (kgl.isAvdelningar() && kg.getDepth()==0) {
            if(!firstRun) { %></div><% }
            %><h2><%= Const.toHtml(kg.getRubrik())  %></h2><%
            firstRun = true;
        } else if (firstRun || kg.getDepth().equals(divBreakLevel) ) {
                %><div class="katalog-index-root"><%
        }
        %>
        <% if (kg.getDepth().equals(divBreakLevel)) { %>
        <div class="katalog-index-row"><h3>
                <% id=Const.getInitData(request).getNewUniktID(); %>
                <a id="contind<%= id %>" onclick="ajxCont(event, 'contind<%= id %>')" href="<%= request.getContextPath()+"/katalog/"+kg.getGrpId() %>"><%= Const.toHtml(kg.getRubrik()) %></a></h3>
                    <div class="katalog-index-row-imgs" >
                        <% for (Produkt tp : kg.getTopProdukter(con)) { %>
                        <div class="katalog-index-row-img" ><img src="<%= Const.getArtBildURL(tp) %>"></div>
                    <% } %>
                    </div>
                        
        </div>
                
        <%  } else if(kg.getDepth() > 0) { %>
        <div class="katalog-index-row">
            <% id=Const.getInitData(request).getNewUniktID(); %>
            <a id="contind<%= id %>" onclick="ajxCont(event, 'contind<%= id %>')" href="<%= request.getContextPath()+"/katalog/"+kg.getGrpId() %>"><% for (int cnn=0; cnn<kg.getDepth()-1-divBreakLevel; cnn++ ) {%>&nbsp;&nbsp;<%} %><%=  Const.toHtml(kg.getRubrik()) %></a></div>
        <%  } %>
       
        <% if (kg.getDepth() > 0) firstRun=false; %>
   <% } %>
                </div>
   
</div>