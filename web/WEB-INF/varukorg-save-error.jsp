<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<% Language lang = StartupData.getLanguage(); %>
<div>
    <%= lang.VarukorgSaveErrorHTML() %>
</div>