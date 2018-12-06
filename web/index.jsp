<%-- 
    Document   : index
    Created on : 2014-dec-17, 16:35:04
    Author     : Ulf
--%>

<%@page import="java.sql.PreparedStatement"%>
<%@page import="se.saljex.hemsida.SessionData"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="se.saljex.hemsida.StartupData"%>
<%@page import="se.saljex.sxlibrary.SXUtil"%>
<%@page import="se.saljex.hemsida.PageHandler"%>
<%@page import="se.saljex.hemsida.Const"%>
<%
    Connection con = Const.getConnection(request);
    ResultSet rs;
    SessionData sd = Const.getSessionData(request);
    Integer kontaktId = sd.getInloggadKontaktId();
    boolean printStandard=true;
    StringBuilder sb = null;
    try {
        if (kontaktId!=null) {
            PreparedStatement ps = con.prepareStatement(
                    "select h.html "+
                    " from kundlogin kl join kundkontakt kk on kk.kontaktid=kl.kontaktid join kund k on k.nummer=kk.kundnr join hemsidameddelande h on  "+
//                    " (h.filter_butik=0 or (k.butik>0 and h.filter_butik>0)) and  "+
//                    " (h.filter_installator=0 or (k.installator>0 and h.filter_installator>0)) and  "+
//                    " (h.filter_grossist=0 or (k.grossist>0 and h.filter_grossist>0)) and  "+
//                    " (h.filter_industri=0 or (k.industri>0 and h.filter_industri>0)) and  "+
//                    " (h.filter_oem=0 or (k.oem>0 and h.filter_oem>0)) and  "+
                    " ( "+
                        " (k.butik>0 and h.filter_butik>0) or  "+
                        " (k.installator>0 and h.filter_installator>0) or  "+
                        " (k.grossist>0 and h.filter_grossist>0) or  "+
                        " (k.industri>0 and h.filter_industri>0) or  "+
                        " (k.oem>0 and h.filter_oem>0) or  "+
                        " (h.filter_butik=0 and h.filter_installator=0 and h.filter_grossist=0 and h.filter_industri=0 and h.filter_oem=0 ) " +
                    " ) and " + 
                    " ( " +
                        " (k.elkund>0 and h.filter_elkund>0) or  "+
                        " (k.vvskund>0 and h.filter_vvskund>0) or  "+
                        " (k.vakund>0 and h.filter_vakund>0) or  "+
                        " (h.filter_elkund=0 and h.filter_vvskund=0 and h.filter_vakund=0 ) "+
//                        " (h.filter_elkund=0 or (k.elkund>0 and h.filter_elkund>0)) and  "+
//                        " (h.filter_vvskund=0 or (k.vvskund>0 and h.filter_vvskund>0)) and  "+
//                        " (h.filter_vakund=0 or (k.vakund>0 and h.filter_vakund>0)) and  "+
                    " ) and " +       
                            
                    " (h.filter_turbil=0 or (k.distrikt=1 and h.filter_turbil>0)) and  "+
                    " (h.filter_nettolista is null or h.filter_nettolista='' or filter_nettolista=k.nettolst) and  "+
                    " (h.filter_kundgrupp is null or h.filter_kundgrupp='' or  h.filter_kundgrupp in (select grpnr from kundgrplank where kundnr=k.nummer ) )  "+
                    " where current_date between startdatum and slutdatum and status = 'Publicerad' and kl.kontaktid=?  "+
                    " order by h.id desc"
            );
            
            ps.setInt(1, kontaktId);
            rs = ps.executeQuery();
            sb = new StringBuilder();
            while (rs.next()) {
                printStandard = false;
                sb.append(rs.getString("html"));
            }
        }
    } catch (Exception e) { e.printStackTrace(); printStandard=true; }
  
    PageHandler pageHandler = null;
    if (printStandard) {
    			pageHandler = new PageHandler(request, response);
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
    }
%>

                    <jsp:include page="/WEB-INF/site-header.jsp" />
                    <% if (printStandard) { %>
                        <%= pageHandler.getParsedHTML() %>
                    <% } else { %>
                        <%= sb.toString() %>
                    <% } %>
                    <jsp:include page="/WEB-INF/site-footer.jsp" />
