<%@page import="se.saljex.hemsida.Const"%>
<%@page import="se.saljex.hemsida.post.GPlusActivityListCache"%>
<%@page import="se.saljex.hemsida.post.Post"%>
<%@page import="se.saljex.hemsida.post.PostList"%>
<%
//URL url = new URL("https://www.googleapis.com/plus/v1/people/113974404358746097068/activities/public?key=AIzaSyA4e3GgYFs5WcIDLlrFoUrHT3EuHrq3pIU");
//URL url2 = new URL("https://www.googleapis.com/plus/v1/activities/z13vcx0b2tmuf3nxt04chbsgfna2xdub5vo0k?key=AIzaSyA4e3GgYFs5WcIDLlrFoUrHT3EuHrq3pIU");

//PostList pl = GPlusActivityListCache.getPostList("113974404358746097068", "AIzaSyA4e3GgYFs5WcIDLlrFoUrHT3EuHrq3pIU");
PostList pl = (PostList)request.getAttribute(Const.ATTRIB_POSTLIST);
Post p = pl.getCurrentPost();
%>
<div class="sxpost-main" <%= pl.getDisplayWidth() != null ? "style=\"width:" + pl.getDisplayWidth() + "\"" : "" %>>
    <div class="sxpost-head" >
    <div class="sxpost-date">
        <%= p.getPublishedDatum() %>
    </div>
    <div class="sxpost-content">
        <%= pl.isShowFullContent() ? "" : "<a href=\"" + p.getUrl() + "\" target=\"__blank\">" %>
        <%= pl.isShowFullContent() ? p.getContent() : p.getTitle() %>
        <%= pl.isShowFullContent() ? "" : "</a>" %>
    </div>
    </div>
<%

    for (Post.Attachment at : p.getAttachments()) {
%>
    
    <div class="sxpost-imgdiv"><a href="<%= at.getUrl() %>" target="_blank"><img class="sxpost-img"  src="<%= at.getImageUrl() %>"></a></div>

<%
     
    }

    %>
</div>
