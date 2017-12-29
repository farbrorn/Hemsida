<%-- 
    Document   : login
    Created on : 2014-dec-14, 12:13:50
    Author     : Ulf
--%>


<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.Const"%>
<% Language lang = StartupData.getLanguage(); %>

<div class="login">
    <h2><%= lang.NyttLosenord() %></h2>
    
    <%= lang.LoginGlomtLosenHTML() %>
</div>