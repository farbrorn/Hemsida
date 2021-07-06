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
            
            
/*            
            function sokare(v) { //contextPath
                ajaxRequest("/sok" + "?g=c&q=" + encodeURIComponent(v),"content");
            }
            
            function vk_add(klasid, variantid, antalid) {//contextPath
                var e = document.getElementById(variantid);
                var antal = document.getElementById(antalid).value;
                if (antal == null || isNaN(antal)) antal=1;
                var variant=null;
                if ("options" in e) variant = e.options[e.selectedIndex]; 
                if (variant==null) variant=e;
                var artnr = variant.getAttribute("aid");
                ajaxRequest("/varukorg" + "?vkaction=add&get=ax&kid=" + klasid +"&aid=" + encodeURIComponent(artnr) + "&qty=" + antal,"vk-content");
            }
            function vk_set(klasid, artnr, antalid) { //contextPath
                var antal = document.getElementById(antalid).value;
                if (antal != null && !isNaN(antal)) {
                    ajaxRequest("/varukorg?vkaction=set&get=ax&kid=" + klasid +"&aid=" + encodeURIComponent(artnr) + "&qty=" + antal,"vk-content");
                }
            }
            function vk_incantal(klasid, artnr, antalid, steg) {//contextPath
                var antal = document.getElementById(antalid).value;
                if (antal != null && !isNaN(antal) && steg!=null && !isNaN(steg)) {
                    antal = antal*1+steg*1;
                    ajaxRequest("/varukorg?vkaction=set&get=ax&kid=" + klasid +"&aid=" + encodeURIComponent(artnr) + "&qty=" + antal,"vk-content");
                }
            }
            function vk_del(klasid, artnr) {  //contextpath
                ajaxRequest("/varukorg?vkaction=set&get=ax&kid=" + klasid +"&aid=" + encodeURIComponent(artnr) + "&qty=0","vk-content");
            }
*/            
            
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
	if(serverStatus == 200 && id!=null){
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
        if (location.protocol == "htps:") {
        xmlhttp.setRequestHeader("Content-length", params.length);
        xmlhttp.setRequestHeader("Connection", "close");
    }
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

function fadeIn(el, time) {
  el.style.opacity = 0;
  var last = +new Date();
  var tick = function() {
    el.style.opacity = +el.style.opacity + (new Date() - last) / time;
    last = +new Date();
    if (+el.style.opacity < 1) {
      (window.requestAnimationFrame && requestAnimationFrame(tick)) || setTimeout(tick, 16)
    }
  };
  tick();
}
