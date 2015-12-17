<%-- 
    Document   : login
    Created on : 2014-dec-14, 12:13:50
    Author     : Ulf
--%>


<%@page import="se.saljex.hemsida.Const"%>
<div class="login">
    <h2>Glömt lösenord</h2>
    <form method="post" action="<%= request.getContextPath() %>/login?<%= Const.PARAM_ACTION %>=<%= Const.ACTION_SKICKANYTTLOSEN %>">
        <table>
            <tr><td><labe for="<%= Const.PARAM_LOGINNAMN %>">Användarnamn: </labe></td><td><input name="<%= Const.PARAM_LOGINNAMN %>" id="<%= Const.PARAM_LOGINNAMN %>" autofocus ></td></tr>
        </table>
        <input type="submit" value="Skicka nytt lösenord" >
    </form>
</div>