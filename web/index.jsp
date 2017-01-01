<%-- 
    Document   : index
    Created on : 2014-dec-17, 16:35:04
    Author     : Ulf
--%>

<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page import="se.saljex.hemsida.Const"%>
<%
    			PageHandler pageHandler = new PageHandler(request, response);
                        pageHandler.loadAndParsePage(StartupData.getPageHome());

			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:title\" content=\"");
			Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgTitle()!=null ? pageHandler.getOgTitle() : pageHandler.getRubrik()));
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:type\" content=\"");
			Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgType()!=null ? pageHandler.getOgTitle() : "article"));
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");

			if (!SXUtil.isEmpty(pageHandler.getOgImage())) {
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgImage()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:image:secure_url\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgImage()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			}
			if (!SXUtil.isEmpty(pageHandler.getOgDescription())) {
				Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:description\" content=\"");
				Const.getInitData(request).addExtraHTMLHeaderContent(Const.toHtml(pageHandler.getOgDescription()));
				Const.getInitData(request).addExtraHTMLHeaderContent("\">");
			}
			Const.getInitData(request).addExtraHTMLHeaderContent("<meta property=\"og:url\" content=\"");
			StringBuffer reqURL = request.getRequestURL();
			String qString=request.getQueryString();
			if (qString!=null) {
				reqURL.append("?");
				reqURL.append(qString);
			}
			Const.getInitData(request).addExtraHTMLHeaderContent(reqURL.toString()); 
			Const.getInitData(request).addExtraHTMLHeaderContent("\">");
    
%>

                    <jsp:include page="/WEB-INF/site-header.jsp" />
                   <%= pageHandler.getParsedHTML() %>
                    
                    <jsp:include page="/WEB-INF/site-footer.jsp" />
