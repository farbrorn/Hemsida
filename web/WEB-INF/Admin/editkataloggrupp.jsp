<%-- 
    Document   : editkataloggrupp
    Created on : 2017-feb-23, 13:43:30
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    KatalogGrupp grp = (KatalogGrupp)request.getAttribute("kataloggrupp");
    %>
    
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ändra kataloggrupp</title>
    </head>
    <body>


        <div class="content">
        <form method="post" action="?grpid=<%= grp.getGrpId() %>">
            Gruppid: <%= grp.getGrpId() %><br>
            Rubrik: <input name="rubrik" value="<%= Const.toHtml(grp.getRubrik()) %>"><br>
            Text:<br>
            <textarea id="textareatext" rows="10" style="width: 100%" name="text"><%= Const.toHtmlNotLineBreak(grp.getText()) %></textarea><br>
            HTML Huvud:<br>
            <textarea id="textareahead" rows="10" style="width: 100%" name="htmlhead"><%= Const.toHtmlNotLineBreak(grp.getHtmlHead()) %></textarea><br>
            HTML Fot:<br>
            <textarea id="textareafoot" rows="10" style="width: 100%" name="htmlfoot"><%= Const.toHtmlNotLineBreak(grp.getHtmlFoot()) %></textarea><br>
            Visa undergrupper: <input type="checkbox" name="visaundergrupper" value="true" <%= grp.isVisaundergrupper() ? "checked" : "" %> ><br>
            <input type="hidden" name="ac" value="save">
            <input type="submit">
            
            
        </form>
        </div>


    </body>
</html>
