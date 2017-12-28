/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import se.saljex.hemsida.post.GPlusActivityListCache;
import se.saljex.hemsida.post.Post;
import se.saljex.hemsida.post.PostList;

/**
 *
 * @author Ulf
 */
public class PageHandler {
	
	private Page sid=null;
	private StringBuilder parsed = new StringBuilder();
	//Open graph
	private String ogType = null;
	private String ogDescription = null;
	private String ogTitle = null;
	private String ogImage = null;
	
	HttpServletRequest request;
	HttpServletResponse response;

	public PageHandler(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	
	public void init() {
            sid=null;
            parsed = new StringBuilder();
            ogType = null;
            ogDescription = null;
            ogTitle = null;
            ogImage = null;
            
        }

	public Page getPage() {return sid; }
	public String getParsedHTML() { return parsed.toString(); }

	public boolean isPageParsed() { return parsed.length() > 0; }
	
	public String getOgType() {
		return ogType;
	}

	public String getOgDescription() {
		return ogDescription;
	}

	public String getOgTitle() {
		return ogTitle;
	}

	public String getOgImage() {
		return ogImage;
	}
	
	public String getRubrik() { return sid.getRubrik(); }
	
	protected String parsePage() throws IOException, ServletException{
		return parsePage(sid.getHtml());
	}	
	public String parsePage(String html) throws IOException, ServletException{
		HttpServletResponseWrapper wrap;
		
		int pos=0;
		int tPos = 0;
		int tPos2 = 0;
		int vPos1=0;
		int vPos2=0;
		int safeCn=0;
		int acpos=0;
		Produkt p;
		Integer kid=null;
		String action;
		String parameter;
			while (safeCn++<10000) {
				tPos= html.indexOf("<sx-",pos);

				if (tPos < 0 ) {
					parsed.append(html.substring(pos, html.length()));
					break;
				}
				acpos = html.indexOf(" ",tPos+4);
				if (acpos>=0) {
					vPos1 = html.indexOf("value=\"",acpos+1);
					if (vPos1 >= 0) {
						vPos2 = html.indexOf("\"", vPos1+7);
						if (vPos2 >= 0) {
							tPos2 = html.indexOf(">", vPos2);
							if (tPos2 >= 0) {
								parsed.append(html.substring(pos, tPos));
								kid=null;
								action = html.substring(tPos+4, acpos).trim();
								parameter = html.substring(vPos1+7, vPos2);
								pos = tPos2+1;
								if ("kbl".equals(action)) {
									try { kid = new Integer(parameter); }  catch (NumberFormatException e) {}
									if (kid!=null) {
										p=null;
										try {
											p = SQLHandler.getProdukt(Const.getConnection(request), kid, Const.getSessionData(request).getAvtalsKundnr());
											request.setAttribute(Const.ATTRIB_PRODUKT, p);
											wrap = getRresponseWrapper(response);
											request.getRequestDispatcher("/WEB-INF/kbl-block-content-small.jsp").include(request, wrap);
											parsed.append(wrap.toString());
										} catch (SQLException e) { parsed.append("Fel vid iläsning av data"); }
									}
								} else if ("asl".equals(action)) {
									String[] split = parameter.split(",");
									if (split.length > 1) try { kid = new Integer(split[0]); }  catch (NumberFormatException e) {}
									p=null;
									try {
										if (split.length > 1) if (kid==null) parsed.append("Felaktigt kid"); else
												p = SQLHandler.getProdukt(Const.getConnection(request), kid, Const.getSessionData(request).getAvtalsKundnr());
										else p = SQLHandler.getProduktFromArtnr(Const.getConnection(request), split[0], Const.getSessionData(request).getAvtalsKundnr(), Const.getSessionData(request).getLagerNr());
										
										if (p== null) {
											parsed.append("Produkten saknas");
										} else {
											request.setAttribute(Const.ATTRIB_PRODUKT, p);
											request.setAttribute(Const.ATTRIB_AID, split[1]);
											wrap = getRresponseWrapper(response);
											request.getRequestDispatcher("/WEB-INF/asl-row.jsp").include(request, wrap);
											parsed.append(wrap.toString());
										}
									} catch (SQLException e) { parsed.append("Fel vid iläsning av data"); e.printStackTrace();}
								} else if ("gpluslist".equals(action)) {
									String[] split = parameter.split(", ");
                                                                        String pId = null;
                                                                        String pKey = null;
                                                                        String pShowFullContent = null;
                                                                        String pWidth = null;
                                                                        String pDisplayItems = null;
                                                                        try {   // hoppar ur om det inte finns tillräckligt mycket i arrayen
                                                                            pId = split[0];
                                                                            pKey = split[1];
                                                                            pShowFullContent = split[2];
                                                                            pWidth = split[3];
                                                                            pDisplayItems = split[4];
                                                                        } catch (Exception e) {}
                                                                        Integer pDisplayItemsInt = null;
                                                                        try { pDisplayItemsInt = new Integer(pDisplayItems); }  catch (NumberFormatException e) {}
                                                                        PostList pl = GPlusActivityListCache.getPostList(pId, pKey);
                                                                        pl.setShowFullContent("true".equals(pShowFullContent));
                                                                        pl.setDisplayItems(pDisplayItemsInt);
                                                                        pl.setDisplayWidth(pWidth);
											request.setAttribute(Const.ATTRIB_POSTLIST, pl);
											wrap = getRresponseWrapper(response);
                                                                        int pcn = 0;
                                                                         for ( Post post : pl.getPosts()) {
                                                                             pcn++;
                                                                             if (pl.getDisplayItems()!=null && pl.getDisplayItems() > 0 && pcn>pl.getDisplayItems() ) break;
                                                                                pl.setCurrentPost(post);
                                                                                request.getRequestDispatcher("/WEB-INF/post.jsp").include(request, wrap);
                                                                         }
                                                                        parsed.append(wrap.toString());
                                                                    
								} else if ("og:type".equals(action)) {
									ogType = parameter;
								} else if ("og:title".equals(action)) {
									ogTitle = parameter;
								} else if ("og:description".equals(action)) {
									ogDescription = parameter;
								} else if ("og:image".equals(action)) {
									ogImage = parameter;
								}
							} else {
								parsed.append(html.substring(pos,vPos2));
								pos = vPos2;            
							}
						} else {
							parsed.append(html.substring(pos,vPos1+7));
							pos = vPos1+7;            
						}
					} else {
						parsed.append(html.substring(pos,acpos+1));
						pos = acpos+1;
					}
				} else {
					parsed.append(html.substring(pos,tPos+4));
					pos = tPos+4;
				}
					
			}
			return parsed.toString();
	}


	private static HttpServletResponseWrapper getRresponseWrapper(HttpServletResponse response) {
		return new HttpServletResponseWrapper(response) {
			private final StringWriter sw = new StringWriter();
			@Override public PrintWriter getWriter() throws IOException { return new PrintWriter(sw);}
			@Override public String toString() { return sw.toString();  }
		};		
		
	}
	public static String removeLeadingTrailingSlash(String sid) {
		if (sid.startsWith("/")) sid=sid.substring(1); //ta bort inledande /
		if (sid.endsWith("/")) sid=sid.substring(0, sid.length()-1);
		return sid;
	}
	
	public String loadAndParsePage(String sidId) throws SQLException, IOException, ServletException {
		sidId=removeLeadingTrailingSlash(sidId);
		PreparedStatement ps = Const.getConnection(request).prepareStatement("select sidid, status, rubrik, html from hemsidasidor where sidid=? ");
		ps.setString(1, sidId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			sid=new Page();
			sid.setSidId(rs.getString(1));
			sid.setStatus(rs.getString(2));
			sid.setRubrik(rs.getString(3));
			sid.setHtml(rs.getString(4));
			return parsePage();
		} else {
			return null;
		}
		
	}
	
	public static void savePage(HttpServletRequest request, Page sida) throws SQLException {
		PreparedStatement ps = Const.getConnection(request).prepareStatement("update hemsidasidor set status=?, rubrik=?, html=? where sidid=? ");
		ps.setString(1, sida.getStatus());
		ps.setString(2, sida.getRubrik());
		ps.setString(3, sida.getHtml());
		ps.setString(4, sida.getSidId());
		if (ps.executeUpdate() <= 0) {
			ps = Const.getConnection(request).prepareStatement("insert into hemsidasidor (sidid, status, rubrik, html) values (?,?,?,?)");
			ps.setString(1, sida.getSidId());
			ps.setString(2, sida.getStatus());
			ps.setString(3, sida.getRubrik());
			ps.setString(4, sida.getHtml());
			ps.executeUpdate();
		}
	}
	
}
