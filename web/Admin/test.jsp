<%-- 
    Document   : test
    Created on : 2015-mar-10, 19:10:11
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.Const"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <%
            
            
            
String h = "En brun räv:<kbl value=\"123\"> kommer fram. Så meining nr <kbl value=\"2\">. Saknar varde <kbl>. Saknar sluttag <kbl value=\"ingen sluttag\". ";            


StringBuilder sb = new StringBuilder();
int pos=0;
int tPos = 0;
int tPos2 = 0;
int vPos1=0;
int vPos2=0;
int safeCn=0;
while (safeCn++<10000) {
    tPos= h.indexOf("<kbl",pos);
    if (tPos < 0 ) {
        sb.append(h.substring(pos, h.length()));
        break;
    }
    vPos1 = h.indexOf("value=\"",tPos+4);
    if (vPos1 >= 0) {
        vPos2 = h.indexOf("\"", vPos1+7);
        if (vPos2 >= 0) {
            tPos2 = h.indexOf(">", vPos2);
            if (tPos2 >= 0) {
                sb.append(h.substring(pos, tPos));
                sb.append("!insatt kod med värde " + h.substring(vPos1+7, vPos2) + "!" );
                pos = tPos2+1;
            } else {
                sb.append(h.substring(pos,vPos2));
                pos = vPos2;            
            }
        } else {
            sb.append(h.substring(pos,vPos1+7));
            pos = vPos1+7;            
        }
    } else {
        sb.append(h.substring(pos,tPos+4));
        pos = tPos+4;
    }
    
}
out.print(Const.toHtml(sb.toString()));

%>        
        
    </body>
</html>
