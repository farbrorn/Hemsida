<%-- 
    Document   : varukorg-full
    Created on : 2014-nov-30, 09:47:46
    Author     : Ulf
--%>
<%@page import="se.saljex.hemsida.VarukorgRow"%>
<%@page import="se.saljex.hemsida.Varukorg"%>
<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%
    SessionData sd = Const.getSessionData(request);
    Varukorg vk = sd.getVarukorg();
%>
<div class="vk" id="vk">
    <h2>Varukorg</h2>
    <jsp:include page="/WEB-INF/varukorg-small-content.jsp" />
</div>