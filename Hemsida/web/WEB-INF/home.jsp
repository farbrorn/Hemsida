<%-- 
    Document   : home
    Created on : 2014-dec-17, 17:10:12
    Author     : Ulf
--%>


<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<div class=""sid">
     <%= PageHandler.parsePage(request, response, PageHandler.getPage(request, StartupData.getPageHome()).getHtml()) %>
</div>