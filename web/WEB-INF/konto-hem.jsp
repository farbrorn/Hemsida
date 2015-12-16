<%-- 
    Document   : konto-hem
    Created on : 2015-dec-02, 14:00:33
    Author     : Ulf
--%><%@page import="se.saljex.hemsida.Const"%>

Välkommen <%= Const.getSessionData(request).getInloggadKontaktNamn() %>
<br>
Här kommer du senare att kunna administrera ditt konto. För närvarande är du välkommen att kontakta oss för information om ditt konto.