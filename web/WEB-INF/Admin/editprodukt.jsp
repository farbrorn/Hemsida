<%-- 
    Document   : editkat
    Created on : 2017-dec-30, 21:31:41
    Author     : ulf
--%>

<%@page import="se.saljex.hemsida.Produkt"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.KatalogGrupp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    Produkt p = (Produkt)request.getAttribute("produkt");
    %>
    
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ändra produkt</title>
    </head>
    <body>


        <div class="content">
        <form method="post" action="?klasid=<%= p.getKlasid() %>">
            Klasid: <%= p.getKlasid() %><br>
            Rubrik: <input name="rubrik" value="<%= Const.toHtml(p.getRubrik()) %>"><br>
            Text:<br>
            <textarea id="textareatext" rows="10" style="width: 100%" name="text"><%= Const.toHtmlNotLineBreak(p.getKortBeskrivning()) %></textarea><br>
            HTML:<br>
            <textarea id="textareahead" rows="10" style="width: 100%" name="html"><%= Const.toHtmlNotLineBreak(p.getBeskrivningHTML()) %></textarea><br>
            <p>
                Visa beskrivning på webbsida från grupp id: <input name="visabeskrivningfrangrpid" type="number" value="<%= p.getWebBeskrivningFranGrpid()==null ? "" : ""+p.getWebBeskrivningFranGrpid() %>">
            </p>
            <input type="hidden" name="ac" value="save">
            <input type="submit">
            
            
        </form>
        </div>


    </body>
</html>
