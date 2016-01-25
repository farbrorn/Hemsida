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
//  StartupData sData = Const.getStartupData();
  LagerEnhet le = sd.getLager();
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
                    <% if (le!=null) { %>
                    <table cellpadding="0" style="border-collapse: collapse;">
                        <tr><td>Lager</td><td>
                                <select id="lagerselector" onchange="setLager()">
                                    <option value="<%= le.getLagernr() %>"><%= Const.toHtml(le.getNamn()) %></option>
                                    <% 
                                    StartupData sData = Const.getStartupData();
                                    java.util.Map<Integer, LagerEnhet> ll = sData.getLagerEnhetList();
                                    for (java.util.Map.Entry<Integer, LagerEnhet> lle : ll.entrySet()) {
                                        if (!lle.getKey().equals(le.getLagernr())) {
                                    %><option value="<%= lle.getKey()%>"><%= Const.toHtml(lle.getValue().getNamn()) %></option>
                                        <% }
                                       
                                    }
                                    %>
                                </select>   
                                
                            
                            </td></tr>
                        <tr><td>Tel</td><td><%= Const.toHtml(le.getTel()) %></td></tr>
                        <tr><td>E-post </td><td><%= Const.toHtml(le.getEpost()) %></td></tr>
                    </table>
                    <% } %>
                    Priser 
                    <select id="inkmomsselector" onchange="setInkmoms()">
                        <option value="true">inkl. moms.</option>
                        <option value="false" <%= sd.isInkMoms(request) ? "" : "selected=\"selected\"" %>>exkl. moms.</option>
                    </select>
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
    <% if (!Const.getInitData(request).getDataCookie().getCookiesAccepted()) { %>
        <div class="" STYLE = "position: fixed;left: 0px;right: 0px; bottom: 0px; min-height: 40px; line-height: 40px; text-align: center; color: white; background: black; z-index: 1000;" id="cookiesaccept"></div>
        <script>
        document.getElementById('cookiesaccept').innerHTML='Vi använder cookies för ökad nvändarupplevelse. <button onclick="setCookiesAccept()" style="margin-left:12px">OK</button>';
            function setCookiesAccept() {
                var AJAX = getHttpRequest(); 
                AJAX.open("GET", "<%= request.getContextPath() %>/SetProperty?<%= Const.PARAM_SETCOOKIEACCEPTED %>=true", false); 
                AJAX.send(null); 
                document.getElementById("cookiesaccept").style.display = "none"; 
            }
        
        
        </script>
    <% } %>
        
    </body>

</html>