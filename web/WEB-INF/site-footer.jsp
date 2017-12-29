<%@page import="se.saljex.hemsida.Language"%>
<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.LagerEnhet"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="se.saljex.hemsida.InitData"%>
<%@page import="se.saljex.hemsida.Const"%>
<% Language lang = StartupData.getLanguage(); %>

<%
  InitData initData = Const.getInitData(request);
  SessionData sd = Const.getSessionData(request);
//  StartupData sData = Const.getStartupData();
  LagerEnhet le = sd.getLager();
%>
</div>

<% ArrayList<String> cards; %>

<% cards = Const.getStartupData().getCardsMidBot();
PageHandler pageHandler  = new PageHandler(request, response);
for (String s : cards) { %>
    <div class="card">
        <%= pageHandler.parsePage(s) %>
        <% pageHandler.init(); %>
    </div>    
<% } %>

</div>
        
        <div class="col-r">
<% cards = Const.getStartupData().getCardsRightTop();
for (String s : cards) { %>
    <div class="card">
        <%= pageHandler.parsePage(s) %>
        <% pageHandler.init(); %>
    </div>    
<% } %>
                <div class="card">
                    <% if (sd.getInloggadUser()!=null) { %>
                        <h4><%= lang.Inloggad() %></h4>
                        <%= sd.getInloggadKontaktNamn() %>, <%= sd.getInloggadKundNamn() %>
                    <% } else { %>
                    <h4><%= lang.Gast() %></h4>
                    <% } %>
                    <% if (le!=null) { %>
                    <table cellpadding="0" style="border-collapse: collapse;">
                        <tr><td><%= lang.Lager() %></td><td>
                                <select id="lagerselector" onchange="setLager(document.getElementById('lagerselector'))">
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
                        <tr><td><%= lang.Tel() %></td><td><%= Const.toHtml(le.getTel()) %></td></tr>
                        <tr><td style="padding-right: 12px"><%= lang.EPost() %></td><td><%= Const.toHtml(le.getEpost()) %></td></tr>
                        <tr><td><%= lang.Priser() %></td><td>                   
                                <select id="inkmomsselector" onchange="setInkmoms(document.getElementById('inkmomsselector'))">
                                <option value="true"><%= lang.InklMoms() %></option>
                                <option value="false" <%= sd.isInkMoms(request) ? "" : "selected=\"selected\"" %>><%= lang.ExklMoms() %></option>
                            </select>
                        </td></tr>
                        <% if (sd.isUserInloggad()) {%>
                        <tr><td><%= lang.Priser() %></td><td> 
                                <select id="isbruttoprisselector" onchange="setIsBruttopris(document.getElementById('isbruttoprisselector'))">
                                <option value="false"><%= lang.Avtalspris() %></option>
                                <option value="true" <%= sd.isVisaBruttopris(request) ? "selected=\"selected\"" : "" %>><%= lang.Listpris() %></option>
                            </select>
                        </td></tr>
                        <% } %>
                    </table>
                    <% } %>
                </div>
            <% if (!initData.isHideVarukorg()) { %>
                <div class="card vk" id="vk">
                    <h4><%= lang.Varukorg() %></h4>
                    <div id="vk-content">
                        <jsp:include page="/WEB-INF/varukorg-small-content.jsp" />
                    </div>
                </div>
        <% } %>
<% cards = Const.getStartupData().getCardsRightBot();
for (String s : cards) { %>
    <div class="card">
        <%= pageHandler.parsePage(s) %>
        <% pageHandler.init(); %>
    </div>    
<% } %>
        </div>
        

                
                
            </div>
        </div>
    <% if (!Const.getInitData(request).getDataCookie().getCookiesAccepted()) { %>
        <div class="cookiesmessage"  id="cookiesaccept"></div>
        <script>
        document.getElementById('cookiesaccept').innerHTML='<%= lang.ViAnvanderCookiesForOkadAnvandarupplevelse() %> <button onclick="setCookiesAccept()" style="margin-left:12px">OK</button>';
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