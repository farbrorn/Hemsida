/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.text.NumberFormat;
import javax.servlet.http.Cookie;

/**
 *
 * @author Ulf
 */
public class DataCookieHandler {
public static final String COOKIENAME="sxdata";

private static final int lagernrStart = 0;
private static final int lagernrLen = 4;
private static final int inkmomsStart = 4;
private static final int inkmomsLen = 1;
private static final int cookiesAcceptedStart = 5;
private static final int cookiesAcceptedLen = 1;

private Integer lagernr;
private Boolean inkmoms;
private Boolean cookiesAccepted;

	public DataCookieHandler() {
	}

	public DataCookieHandler(String varde) {
		setVarde(varde);
	}
		
	public String getVarde() {
		String inkmomsS=" ";
		if (inkmoms!=null) inkmomsS = inkmoms ? "T" : "F";
		String cookiesAcceptedS=" ";
		if (cookiesAccepted!=null) cookiesAcceptedS = cookiesAccepted ? "T" : "F";
		return String.format("%04d", lagernr) + inkmomsS + cookiesAcceptedS;
	}
	public Cookie getCookie() {
		Cookie c  = new Cookie(COOKIENAME, getVarde());
		c.setMaxAge(60 * 60 * 24 * 365 * 10);
		return c; 
	}
	
	public void setVarde(String varde) { 
		//Lagernr
		Integer i=null;
		try { i = Integer.parseInt(varde.substring(lagernrStart, lagernrStart+lagernrLen)); } catch (Exception e) {}
		lagernr=i;

		String s;
		//Inkmoms
		s=null;
		try { s = varde.substring(inkmomsStart, inkmomsStart+inkmomsLen); } catch (Exception e) {}
		if (s==null) inkmoms=null;
		else inkmoms= !"F".equals(s);
		
		//Cookiesaccepted
		s=null;
		try { s = varde.substring(cookiesAcceptedStart, cookiesAcceptedStart+cookiesAcceptedLen); } catch (Exception e) {}
		cookiesAccepted = "T".equals(s);
	}

	public void setLagernr(Integer lagernr) {
		this.lagernr = lagernr;
	}

	public void setInkmoms(Boolean inkmoms) {
		this.inkmoms = inkmoms;
	}

	public void setCookiesAccepted(Boolean cookiesAccepted) {
		this.cookiesAccepted = cookiesAccepted;
	}

	public Integer getLagernr() {
		return lagernr;
	}

	public Boolean getInkmoms() {
		return inkmoms;
	}

	public Boolean getCookiesAccepted() {
		return cookiesAccepted;
	}
	
	
}
