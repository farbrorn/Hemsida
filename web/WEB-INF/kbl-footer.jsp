<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.Const"%>
<% Language lang = StartupData.getLanguage(); %>
<% boolean inkMoms = Const.getSessionData(request).isInkMoms(request); %>
<div class="momsinfo">

<% if (inkMoms) { %>
    <%= lang.PriserInklusiveMoms() %>
<% } else { %>
    <%= lang.PriserExklusiveMoms() %>
<% } %>
</div>
</div>