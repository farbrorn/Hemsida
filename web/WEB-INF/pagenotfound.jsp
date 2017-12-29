<%-- 
    Document   : pagenotfound
    Created on : 2015-mar-01, 16:41:39
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% Language lang = StartupData.getLanguage(); %>
<!DOCTYPE html>
<%= lang.PageNotFoundHTML() %>
