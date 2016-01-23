<%@page import="se.saljex.hemsida.Const"%>
<% boolean inkMoms = Const.getSessionData(request).isInkMoms(request); %>
<div class="momsinfo">

<% if (inkMoms) { %>
    Priser inklusive moms.
<% } else { %>
    Priser exklusive moms.
<% } %>
</div>
</div>