<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.hemsida.Const"%>
<html>
    <head>
        <title><%= StartupData.getDefaultHTMLTitle() %></title>
        <link rel="icon" type="image/png" href="<%= StartupData.getFavIconUrl()%>">
<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

<% if (StartupData.isHemsidaTestlage()) { %> <META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW"> <% } %>
<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
        <script>
            function updateVariant(id) {
                var e = document.getElementById("variant-" + id);
                var variant = e.options[e.selectedIndex];
                var pris = variant.getAttribute("pris");
                var ilagerstr = variant.getAttribute("ilager");
                var frp = variant.getAttribute("frp");
                var frpPris = pris * frp;
                var frpPrisStr = "(" + frpPris + " kr/frp)";
                //            document.getElementById("frp-pris-"+id).style.display="block";
                //            if (frpPris<=pris) document.getElementById("frp-pris-"+id).style.display="none";
                document.getElementById("pris-" + id).innerHTML = formatPris(pris);
                document.getElementById("ilager-" + id).innerHTML = formatPris(ilagerstr);
//                document.getElementById("frp-pris-" + id).innerHTML = frpPrisStr;
            }
            
            function formatPris(pris) {
                
                if (isNaN(pris)) return pris;
                pris = Math.round(pris*100)/100;
                var p = parseInt(pris);
                var ore=Math.round((pris-p)*100);
                if (ore!=0 || pris < 100) {
                    return formatPris2(pris, 2,' ',',');  
                } else {
                    return formatPris2(pris, 0,' ',',');  
                }
            }
            
            
            
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
            
            
function formatPris2(n, decPlaces, thouSeparator, decSeparator) {
    decPlaces = isNaN(decPlaces = Math.abs(decPlaces)) ? 2 : decPlaces,
    decSeparator = decSeparator == undefined ? "." : decSeparator,
    thouSeparator = thouSeparator == undefined ? "," : thouSeparator,
    sign = n < 0 ? "-" : "",
    i = parseInt(n = Math.abs(+n || 0).toFixed(decPlaces)) + "",
    j = (j = i.length) > 3 ? j % 3 : 0;
    return sign + (j ? i.substr(0, j) + thouSeparator : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thouSeparator) + (decPlaces ? decSeparator + Math.abs(n - i).toFixed(decPlaces).slice(2) : "");
}
            function vkShow() {
                var e = document.getElementById("vk");
                if (e.style.visibility == "hidden") e.style.visibility = "visible";
                else e.style.visibility = "hidden";
            }
            
            
function callback(serverData, serverStatus, id) { 
	if(serverStatus == 200){
   		document.getElementById(id).innerHTML = serverData;   
                if (id=="content") renderAjaxContentOnLoaded();
	} else {
		//document.getElementById(id).innerHTML = 'Laddar...'; 
	}
}
 
 
 function supports_history_api() {
  return !!(window.history && history.pushState);
}
window.onload = function() {
  if (!supports_history_api()) { return; }
  window.setTimeout(function() {
    window.addEventListener("popstate", function(e) {
      ajaxRequest(addAjxToUrl(location.pathname),"content");
    }, false);
  }, 1);
}

function addAjxToUrl(url) {
        var o;
        if (url.indexOf("?") > 0) o = url + "&g=c"; else o = url + "?g=c";
        return o;
    }
    
function ajxCont(e,element) {
    if (supports_history_api()) {
        e.preventDefault();
        var o;
        var openThis = document.getElementById(element).href;
        o = addAjxToUrl(openThis);
        ajaxRequest(o,"content");
        history.pushState({state:1}, "", openThis);
        return false;
    }
   }
   
   
   
function AJAXPost(formId) {
    AJAXPost2(formId, "content");
}
   
function AJAXPost2(formId, resultId) {
    var elem   = document.getElementById(formId).elements;
    var url    = document.getElementById(formId).action;        
        var params = "";
    var value;

    for (var i = 0; i < elem.length; i++) {
        if (elem[i].tagName == "SELECT") {
            value = elem[i].options[elem[i].selectedIndex].value;
        } else {
            value = elem[i].value;                
        }
        params += elem[i].name + "=" + encodeURIComponent(value) + "&";
    }
    xmlhttp = getHttpRequest();
    xmlhttp.open("POST",url,false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    <% if(!Const.getStartupData().redirectToHttps()) { %>
        xmlhttp.setRequestHeader("Content-length", params.length);
        xmlhttp.setRequestHeader("Connection", "close");
    <% } %>
    xmlhttp.send(params);

    if (xmlhttp.status==200) document.getElementById(resultId).innerHTML = xmlhttp.responseText;
    else alert("Fel vid kommunikation med server.");
}   
   
   
   
function getHttpRequest() {
   var AJAX = null; 
   if (window.XMLHttpRequest) {
      AJAX=new XMLHttpRequest(); 
   } else {
      AJAX=new ActiveXObject("Microsoft.XMLHTTP"); 
   }
   if (AJAX == null) {
      alert("Webbläsaren stöde inte AJAX."); 
      return false; 
   }
   return AJAX;
  }
    
function ajaxRequest(openThis, id) {
    
   var AJAX = getHttpRequest(); 
   AJAX.onreadystatechange = function() { 
      if (AJAX.readyState == 4 || AJAX.readyState == "complete") { 
         callback(AJAX.responseText, AJAX.status, id); 
      }  else { /*document.getElementById(id).innerHTML = 'söker'; */
         } 
   }
   var url= openThis;
   AJAX.open("GET", url, true); 
   AJAX.send(null);
}


function renderAjaxContentOnLoaded() {
    FB.XFBML.parse();
    gapi.plusone.go();
    }

            
            
        </script>
        
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <meta name="viewport" content="width=320, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/inc/a.css">    </head>

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
            <div class="site-header">
                <a href="<%= request.getContextPath() %>/">
                    <div class="site-header-logo"><img src="<%= Const.getStartupData().getLogoUrl() %>" alt="Logga"></div>
                </a>
                <div class="site-header-sok">
                    <form action="<%= request.getContextPath() + "/sok" %>" method="get">
                        <!--<table><tr>     <td> -->
                                    <input name="q" class="site-header-sok-input" onkeyup="sokare(this.value)">
                                <!-- </td>  <td> -->
                                    <input value="Sök" type="submit">
                               <!-- </td>             </tr></table>  -->
                    </form>
                </div>
                <div class="site-header-abar">                
                    <div class="site-header-userinfo" onclick="vkShow()">
                        <% if (Const.getSessionData(request).getInloggadKontaktId()==null) {
                            %> <div class="site-header-userinfo-login"><a href="<%= request.getContextPath() %>/login">Logga in</a></div><%
                        } else {
                            %> <div class="site-header-userinfo-login"><a href="<%= request.getContextPath() %>/logout">Logga ut</a></div><%                            
                        } %>
                        <% //<div class="site-header-userinfo-namn"><%= Const.toHtml(Const.getSessionData(request).getInloggadKontaktNamn()) ></div> %>
                    </div>
                <a href="<%= request.getContextPath() %>/varukorg">
                    <div class="site-header-kassa-btn a-btn" >
                         <span>Till kassan</span>
                    </div>
                </a>
                <a href="<%= request.getContextPath() %>/s/<%= Const.getStartupData().getPageMeny() %>">
                    <div class="site-header-menu-btn b-btn"  >Meny</div>
                </a>
                </div>
            </div>
<% /*
            <div>
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_cart">
<input type="hidden" name="upload" value="1">
<input type="hidden" name="currency_code" value="SEK">
<input type="hidden" name="tax_cart" value="25.00">
<input type="hidden" name="business" value="ulf.hemma@gmail.com">
<input type="hidden" name="item_name_1" value="Golvvärmerör 17">
<input type="hidden" name="quantity_1" value="10">
<input type="hidden" name="amount_1" value="120.00">
<input type="hidden" name="shipping_1" value="0">
<input type="hidden" name="item_name_2" value="Pressböj 15mm med 2 st muffar i båda ändarna">
<input type="hidden" name="quantity_2" value="10">
<input type="hidden" name="amount_2" value="20.00">
<input type="hidden" name="shipping_2" value="2.50">
<input type="submit" value="PayPal">
</form>            </div>
*/ %>

         <div class="main">
             <div class="col-l">
<% ArrayList<String> cards; %>

<% cards = Const.getStartupData().getCardsLeftTop();
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
                           

                