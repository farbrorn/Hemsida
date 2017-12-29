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
    <h2><%= lang.LoggaIn() %></h2>
    <form method="post" action="<%= request.getContextPath() %>/login">
        <table>
            <tr><td><labe for="<%= Const.PARAM_LOGINNAMN %>"><%= lang.Anvandarnamn() %>: </labe></td><td><input name="<%= Const.PARAM_LOGINNAMN %>" id="<%= Const.PARAM_LOGINNAMN %>" autofocus ></td></tr>
            <tr><td><labe for="<%= Const.PARAM_LOGINLOSENL %>"><%= lang.Losen() %>: </labe></td><td><input type="password" name="<%= Const.PARAM_LOGINLOSENL %>" id="<%= Const.PARAM_LOGINLOSENL %>"></td></tr>   
        </table>
        <input type="submit" value="Logga in" > <a href="?<%= Const.PARAM_ACTION %>=<%= Const.ACTION_GLOMTLOSEN %>"><%= lang.GlomtLosenord() %></a>
        
    </form>
</div>