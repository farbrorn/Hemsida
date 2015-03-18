<%-- 
    Document   : login-inloggad
    Created on : 2014-dec-14, 12:41:29
    Author     : Ulf
--%><%@page import="se.saljex.hemsida.Const"%>

<div>
Du är inloggad <%= Const.getSessionData(request).getInloggadKontaktNamn() %>
</div>
<a href="<%= request.getContextPath() %>/logout">Logga ut</a>