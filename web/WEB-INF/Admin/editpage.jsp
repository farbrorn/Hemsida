<%-- 
    Document   : editpage
    Created on : 2015-mar-01, 18:46:32
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.Page"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/inc/a.css">    </head>*
<% 
String editor = request.getParameter("editor");
if (Const.isEmpty(editor)) editor="html";
if ( "wysiwyg".equals(editor)) { %>
<script type="text/javascript" src="<%= request.getContextPath() %>/inc/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea",
    plugins: [
         "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
         "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
         "save table contextmenu directionality emoticons template paste textcolor"
   ]
 });
</script>
<% } else if ("html".equals(editor)) { %>
<script src="<%= request.getContextPath() %>/inc/CodeMirror-master/lib/codemirror.js"></script>
<link rel="stylesheet" href="<%= request.getContextPath() %>/inc/CodeMirror-master/lib/codemirror.css">

<script src="<%= request.getContextPath() %>/inc/CodeMirror-master/mode/xml/xml.js"></script>
<script src="<%= request.getContextPath() %>/inc/CodeMirror-master/mode/css/css.js"></script>
<script src="<%= request.getContextPath() %>/inc/CodeMirror-master/mode/htmlmixed/htmlmixed.js"></script>
<% } %>

<%
                                Page sida=PageHandler.getPage(request, request.getPathInfo());
                                if (sida==null) sida = new Page();

%>


    </head>
    <body>
        <div class="content">
        <form method="post" action="?">
            <table>
                <tr>
                    <td>ID</td>
                    <td><input name="sidid" value="<%= Const.toHtml(sida.getSidId()) %>"> (Sorteringsordning för kort)</td>
                </tr>
                <tr>
                    <td>Status</td>
                    <td><input name="status" value="<%= sida.getStatus()==null ? Page.STATUS_SIDA : Const.toHtml(sida.getStatus()) %>">
                    <%= Page.STATUS_SIDA %> = Vanlig sida * 
                    <%= Page.STATUS_RADERAD %> = Raderad * 
                    <%= Page.STATUS_CARD_LEFT_TOP %> = Kort Vänster Topp * 
                    <%= Page.STATUS_CARD_LEFT_BOT %> = Kort Vänster Botten * 
                    <%= Page.STATUS_CARD_MID_TOP %> = Kort Mitten Topp * 
                    <%= Page.STATUS_CARD_MID_BOT %> = Kort Mitten Botten * 
                    <%= Page.STATUS_CARD_RIGHT_TOP %> = Kort Höger Topp * 
                    <%= Page.STATUS_CARD_RIGHT_BOT %> = Kort Höger Botten 
                    </td>
                    
                </tr>
                <tr>
                    <td>Sidrubrik</td>
                    <td><input name="rubrik" value="<%= Const.toHtml(sida.getRubrik()) %>"></td>
                </tr>
                <input type="hidden" name="ac" value="save">
            </table>
                <input type="submit">
                <br>
        <textarea id="textarea" rows="30" style="width: 100%" name="html"><%= Const.toHtmlNotLineBreak(sida.getHtml()) %></textarea>
        </form>
        <a href="<%= request.getContextPath() + request.getServletPath() + request.getPathInfo() + "?" + "editor=none" %>" >Redigera sidan utn Editor</a>
        <a href="<%= request.getContextPath() + request.getServletPath() + request.getPathInfo() + "?" + "editor=wysiwyg" %>" >Redigera sidan WYSIWYG</a>
        <a href="<%= request.getContextPath() + request.getServletPath() + request.getPathInfo() + "?" + "editor=html" %>" >Redigera sidan i ren HTML-Editor</a>
        </div>
        
        <table>
            <tr><td><%= Const.toHtml("<sx-als valu\"kid, aid\"") %></td><td>Visa liten artikelrad</td> </tr>
            <tr><td><%= Const.toHtml("<sx-als valu\"aid\"") %></td><td>Visa liten artikelrad från artikelnummer</td> </tr>
            <tr><td><%= Const.toHtml("<sx-kbl valu\"kid\"") %></td><td>Litet artikelblock på en kid</td> </tr>
        </table>
    </body>
</html>
<% if ("html".equals(editor))  { %>
<script>
    var myCodeMirror = CodeMirror.fromTextArea(document.getElementById("textarea"));
/*var myCodeMirror = CodeMirror(document.body, {
  value: "function myScript(){return 100;}\n",
  mode:  "javascript"
});*/
</script>
<% } %>
