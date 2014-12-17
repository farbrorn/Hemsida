<%-- 
    Document   : login
    Created on : 2014-dec-14, 12:13:50
    Author     : Ulf
--%>


<%@page import="se.saljex.hemsida.Const"%>
<div class="login">
    <h2>Logga in</h2>
    <form method="post" action="<%= request.getContextPath() %>/login">
        <div><labe for="<%= Const.PARAM_LOGINNAMN %>">Användarnamn: </labe><input name="<%= Const.PARAM_LOGINNAMN %>" id="<%= Const.PARAM_LOGINNAMN %>"></div>
    <div><labe for="<%= Const.PARAM_LOGINLOSENL %>">Lösen: </labe><input name="<%= Const.PARAM_LOGINLOSENL %>" id="<%= Const.PARAM_LOGINLOSENL %>"></div>   
    <input type="submit" valu="Logg in">
    </form>
</div>