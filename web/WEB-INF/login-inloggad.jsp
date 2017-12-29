<%-- 
    Document   : login-inloggad
    Created on : 2014-dec-14, 12:41:29
    Author     : Ulf
--%><%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.Const"%>
<% Language lang = StartupData.getLanguage(); %>

<div>
<%= lang.DuArInloggad() %> <%= Const.getSessionData(request).getInloggadKontaktNamn() %>
</div>
<a href="<%= request.getContextPath() %>/logout"><%= lang.LoggaUt() %></a>