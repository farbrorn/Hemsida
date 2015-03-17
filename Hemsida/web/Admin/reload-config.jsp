<%-- 
    Document   : reload-config
    Created on : 2015-mar-10, 13:29:53
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin</title>
    </head>
    <body>
        <h1>Reload configuration</h1>
        <% 
            StartupData d = new StartupData(Const.getStartupData().getSxadm());
            d.loadConfig();
            Const.setStartupData(d);
        %>
    </body>
</html>
