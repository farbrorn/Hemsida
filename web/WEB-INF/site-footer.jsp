<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.LagerEnhet"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="se.saljex.hemsida.InitData"%>
<%@page import="se.saljex.hemsida.Const"%>
<%
  InitData initData = Const.getInitData(request);
  SessionData sd = Const.getSessionData(request);
  StartupData sData = Const.getStartupData();
  LagerEnhet le = sData.getLagerEnhet(sd.getLagerNr());
%>
</div>

<% ArrayList<String> cards; %>

<% cards = Const.getStartupData().getCardsMidBot();
for (String s : cards) { %>
    <div class="card">
        <%= PageHandler.parsePage(request, response, s) %>
    </div>    
<% } %>

</div>
        
        <div class="col-r">
<% cards = Const.getStartupData().getCardsRightTop();
for (String s : cards) { %>
    <div class="card">
        <%= PageHandler.parsePage(request, response, s) %>
    </div>    
<% } %>
                <div class="card">
                    <h4>Inloggad</h4>
                    <% if (sd.getInloggadUser()!=null) { %>
                        <%= sd.getInloggadKontaktNamn() %>, <%= sd.getInloggadKundNamn() %>
                    <% } else { %>
                        Gäst
                    <% } %>
                    <% if (sData.getLagerEnhetList().size() > 1) { %>
                    <table>
                        <tr><td>Lager</td><td><%= Const.toHtml(le.getNamn()) %></td></tr>
                        <tr><td>Tel</td><td><%= Const.toHtml(le.getTel()) %></td></tr>
                        <tr><td>E-post</td><td><%= Const.toHtml(le.getEpost()) %></td></tr>
                    </table>
                    <% } %>
                    <%= sd.isInkMoms() ? "Priser inklusive moms." : "Priser exklusive moms" %><br>
                </div>
            <% if (!initData.isHideVarukorg()) { %>
                <div class="card vk" id="vk">
                    <h4>Varukorg</h4>
                    <div id="vk-content">
                        <jsp:include page="/WEB-INF/varukorg-small-content.jsp" />
                    </div>
                </div>
        <% } %>
<% cards = Const.getStartupData().getCardsRightBot();
for (String s : cards) { %>
    <div class="card">
        <%= PageHandler.parsePage(request, response, s) %>
    </div>    
<% } %>
        </div>
        

                
                
            </div>
        </div>
    </body>

</html>