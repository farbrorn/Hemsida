/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import se.saljex.loginservice.User;

/**
 *
 * @author Ulf
 */
public class InitData {

	private boolean metaRobotsNoIndex = false;
	boolean hideVarukorg=false;
	private long uniktID=0;
	private ServletRequest request;
    public InitData(ServletRequest request) {
		this.request=request;
    }
    private se.saljex.loginservice.User adminUser;

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

            
            
	private StringBuilder extraHTMLHeadContent = new StringBuilder(400);
    java.sql.Connection con = null;

	private DataCookieHandler dataCookie;

	public DataCookieHandler getDataCookie() {
		return dataCookie;
	}

	public void setDataCookie(DataCookieHandler dataCookie) {
		this.dataCookie = dataCookie;
	}
	
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
	public long getNewUniktID() {
		return uniktID++;
	}

	public boolean isContentOnlyCall() {
		return "c".equals(request.getParameter("g"));
	}

	public boolean isHideVarukorg() {
		return hideVarukorg;
	}

	public void setHideVarukorg(boolean hideVarukorg) {
		this.hideVarukorg = hideVarukorg;
	}

	public boolean isMetaRobotsNoIndex() {
		return metaRobotsNoIndex;
	}

	public void setMetaRobotsNoIndex(boolean metaRobotsNoIndex) {
		this.metaRobotsNoIndex = metaRobotsNoIndex;
	}

	public void addExtraHTMLHeaderContent(String s) {
		if (s!=null) extraHTMLHeadContent.append(s);
	}
	public String getExtraHTMLHeaderContent() {
		return extraHTMLHeadContent.toString();
	}
	
	

}
