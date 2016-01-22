<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Const"%>
<html>
    <head>
        <title><%= StartupData.getDefaultHTMLTitle() %></title>
        <link rel="icon" type="image/png" href="<%= StartupData.getFavIconUrl()%>">
        <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

        <% if (StartupData.isHemsidaTestlage() || Const.getInitData(request).isMetaRobotsNoIndex()) { %> <META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW"> <% } %>
        <meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <meta name="viewport" content="width=320, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/inc/a.css">   
        <script src="/inc/sx.js" async></script>
        <script>
            function sokare(v) {
                ajaxRequest("<%= request.getContextPath() + "/sok" %>" + "?g=c&q=" + encodeURIComponent(v),"content");
            }
            
            function vk_add(klasid, variantid, antalid) {
                var e = document.getElementById(variantid);
                var antal = document.getElementById(antalid).value;
                if (antal == null || isNaN(antal)) antal=1;
                var variant=null;
                if ("options" in e) variant = e.options[e.selectedIndex]; 
                if (variant==null) variant=e;
                var artnr = variant.getAttribute("aid");
                ajaxRequest("<%= request.getContextPath() + "/varukorg" %>" + "?<%= Const.PARAM_VARUKORG_AC + "=" + Const.PARAM_VARUKORG_AC_ADD + "&" + Const.PARAM_VARUKORG_GET + "=" + Const.PARAM_VARUKORG_GET_AJAX + "&" + Const.PARAM_KLASID + "=" %>" + klasid +"<%= "&" + Const.PARAM_ARTNR +"=" %>" + encodeURIComponent(artnr) + "<%= "&" + Const.PARAM_ANTAL + "=" %>" + antal,"vk-content");
            }
            function vk_set(klasid, artnr, antalid) {
                var antal = document.getElementById(antalid).value;
                if (antal != null && !isNaN(antal)) {
                    ajaxRequest("<%= request.getContextPath() + "/varukorg" %>" + "?<%= Const.PARAM_VARUKORG_AC + "=" + Const.PARAM_VARUKORG_AC_SET + "&" + Const.PARAM_VARUKORG_GET + "=" + Const.PARAM_VARUKORG_GET_AJAX + "&" + Const.PARAM_KLASID + "=" %>" + klasid +"<%= "&" + Const.PARAM_ARTNR +"=" %>" + encodeURIComponent(artnr) + "<%= "&" + Const.PARAM_ANTAL + "=" %>" + antal,"vk-content");
                }
            }
            function vk_incantal(klasid, artnr, antalid, steg) {
                var antal = document.getElementById(antalid).value;
                if (antal != null && !isNaN(antal) && steg!=null && !isNaN(steg)) {
                    antal = antal*1+steg*1;
                    ajaxRequest("<%= request.getContextPath() + "/varukorg" %>" + "?<%= Const.PARAM_VARUKORG_AC + "=" + Const.PARAM_VARUKORG_AC_SET + "&" + Const.PARAM_VARUKORG_GET + "=" + Const.PARAM_VARUKORG_GET_AJAX + "&" + Const.PARAM_KLASID + "=" %>" + klasid +"<%= "&" + Const.PARAM_ARTNR +"=" %>" + encodeURIComponent(artnr) + "<%= "&" + Const.PARAM_ANTAL + "=" %>" + antal,"vk-content");
                }
            }
            function vk_del(klasid, artnr) {
                ajaxRequest("<%= request.getContextPath() + "/varukorg" %>" + "?<%= Const.PARAM_VARUKORG_AC + "=" + Const.PARAM_VARUKORG_AC_SET + "&" + Const.PARAM_VARUKORG_GET + "=" + Const.PARAM_VARUKORG_GET_AJAX + "&" + Const.PARAM_KLASID + "=" %>" + klasid +"<%= "&" + Const.PARAM_ARTNR +"=" %>" + encodeURIComponent(artnr) + "<%= "&" + Const.PARAM_ANTAL + "=" %>" + 0,"vk-content");
            }
            
        </script>
    
    </head>

    <body>
<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/sv_SE/sdk.js#xfbml=1&version=v2.0";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
<script src="https://apis.google.com/js/platform.js" async defer>
  {lang: 'sv'}
</script>
<div class="site-header"><div class="site-header-inner">
                <a href="<%= request.getContextPath() %>/">
                    <div class="site-header-logo"><img src="<%= Const.getStartupData().getLogoUrl() %>" alt="Logga"></div>
                </a>
                <div class="site-header-sok">
                    <form action="<%= request.getContextPath() + "/sok" %>" method="get">
                        <input name="q" class="site-header-sok-input" onkeyup="sokare(this.value)">
                        <input value="Sök" type="submit">
                    </form>
                </div>
                <div class="site-header-abar">                
                    <div class="site-header-userinfo" onclick="vkShow()">
                        <% if (Const.getSessionData(request).getInloggadKontaktId()==null) {
                            %> <div class="site-header-userinfo-login"><a href="<%= request.getContextPath() %>/login">Logga in</a></div><%
                        } else {
                            %> <div class="site-header-userinfo-logout"><a href="<%= request.getContextPath() %>/logout">Logga ut</a><br><span class="site-header-userinfo-namn"><%= Const.toHtml(Const.getSessionData(request).getInloggadKontaktNamn()) %></span></div>
                      <%  } %>
                        <% //<div class="site-header-userinfo-namn"><%= Const.toHtml(Const.getSessionData(request).getInloggadKontaktNamn()) ></div> %>
                    </div>
                    
                    <% if (Const.getSessionData(request).getInloggadKontaktId()!=null) { %>
                        <a href="<% out.print(request.getContextPath()); %>/konto">
                            <div class="site-header-konto-btn"  ><img src="/inc/konto.png"></div>
                        </a>
                    <% } %>
                   
                <a href="<%= request.getContextPath() %>/varukorg">
                    <div class="site-header-kassa-btn a-btn" >
                         Till Kassan
                    </div>
                </a>
                <a href="<%= request.getContextPath() %>/s/<%= Const.getStartupData().getPageMeny() %>">
                    <div class="site-header-menu-btn"  ><img src="/inc/meny.png"></div>
                </a>
                </div>
            </div></div>

         <div class="main">
             <div class="col-l">
                 
<% 
    ArrayList<String> cards; 
 
   cards = Const.getStartupData().getCardsLeftTop();
for (String s : cards) { %>
    <div class="card">
        <%= s %>
    </div>    
<% } %>
                 
                   <jsp:include page="/WEB-INF/produkttrad.jsp" />
<% cards = Const.getStartupData().getCardsLeftBot();
for (String s : cards) { %>
    <div class="card">
        <%= s %>
    </div>    
<% } %>
                
             </div>    

<div class="col-m">
<% cards = Const.getStartupData().getCardsMidTop();
for (String s : cards) { %>
    <div class="card">
        <%= s %>
    </div>    
<% } %>
    
<div class="card content" id="content">
                