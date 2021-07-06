<%-- 
    Document   : index
    Created on : 2017-jan-01, 18:59:47
    Author     : Ulf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin</title>
    </head>
    <body>
        <h2>Admin</h2>
        <table>
            <tr><td><a href="rebuild-sort-index.jsp">rebuild-sort-index</a></td><td></td></tr>
            <tr><td><a href="reload-config.jsp">Reload Config</a></td><td></td></tr>
            <tr><td><a href="EditPage/hem">Edit Page</a></td><td></td></tr>
            <tr><td><a href="EditArtGrp?grpid=">Edit ArtGrupp (Ange numret på gruppen i url-parameter ?grpid=nn)</a></td><td></td></tr>
            <tr><td><a href="settings.jsp">Inställningar</a></td><td></td></tr>
        </table>
    </body>
</html>
