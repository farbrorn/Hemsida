/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 */
public class SessionData {
	private Varukorg varukorg = null;
	private User inloggadUser=null;

	private Integer lagernr=null;
	
	KatalogGruppLista kgl=null;
	
	boolean inkMoms = true;

	public User login(Connection con, HttpServletRequest request, String anvandarnamn, String losen) {
		inloggadUser=null;
		try {
			inloggadUser = SQLHandler.login(con, anvandarnamn, losen);
			if (inloggadUser!=null && varukorg!=null) varukorg.mergeSQLVarukorg(request);
		} catch (SQLException e) {}
		if (inloggadUser!=null) inkMoms=inloggadUser.isDefaultInkMoms();
		return inloggadUser;
	}
	public User autoLogin(Connection con, HttpServletRequest request) {
		try {
			if (inloggadUser==null) {
				Cookie[] cookies = request.getCookies();

				String autoLoginId = null;
				if (cookies!=null) for(Cookie cookie : cookies){
					if(Const.COOKIEAUTOINLOGID.equals(cookie.getName()))	autoLoginId = cookie.getValue();
				}
				if (autoLoginId!=null) {
					inloggadUser = SQLHandler.autoLogin(con, autoLoginId);
					if (inloggadUser!=null && varukorg!=null) varukorg.mergeSQLVarukorg(request);
					if (inloggadUser!=null) inkMoms=inloggadUser.isDefaultInkMoms();
				}
			}
		
		} catch (SQLException e) { e.printStackTrace(); }
		return inloggadUser;
	}
	
	public void setAuoLogin(Connection con, HttpServletResponse response) {
		if (inloggadUser!=null) {
			try {
						PreparedStatement u;
						Random r = new Random();
						//Slumpmässig sträng med upp till 26 tecken
						String autoLoginId = (Long.toString(Math.abs(r.nextLong()), 36) + Long.toString(Math.abs(r.nextLong()), 36)).trim();

						
						u = con.prepareStatement("insert into butikautologin (uuid, kontaktid, expiredate) values (?,?, current_date+30)");
						u.setString(1, autoLoginId);
						u.setInt(2, inloggadUser.getKontaktId());
						u.executeUpdate();
						
						Cookie c2 = new Cookie(Const.COOKIEAUTOINLOGID, autoLoginId);
						c2.setMaxAge(30*24*60*60);
						response.addCookie(c2);
			} catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	public void logout(Connection con ) {
		try { SQLHandler.logoutAutoLogin(con, inloggadUser); } catch (SQLException e) { e.printStackTrace(); }
		varukorg=null;
		inloggadUser=null;
		inkMoms=true;
	}

	public Integer getLagerNr() {
		return lagernr==null? inloggadUser!=null ? inloggadUser.getDefultLagernr() : StartupData.getDefultLagernr() : lagernr;
	}
	public void setLagernr(Integer lagernr) { this.lagernr=lagernr; }
	
	public Varukorg getVarukorg(HttpServletRequest request) {
		if (varukorg==null) {
			if (inloggadUser!=null) {
				try {
					varukorg = new Varukorg(request);
				} catch (SQLException e) {
					varukorg = new Varukorg();
				}
			} else { varukorg = new Varukorg(); }
		}
		return varukorg;
	}

	public void setVarukorg(Varukorg varukorg) {
		this.varukorg = varukorg;
	}

	public User getInloggadUser() {
		return inloggadUser;
	}

	public void setInloggadUser(User user) {
		this.inloggadUser = user;
	}
	public Integer getInloggadKontaktId() {
		return inloggadUser==null ? null : inloggadUser.getKontaktId();
	}
	public String getInloggadKundnr() {
		return inloggadUser==null ? null : inloggadUser.getKundnr();
	}
	public String getInloggadKontaktNamn() {
		return inloggadUser==null ? null : inloggadUser.getKontaktNamn();
	}
	public String getInloggadKundNamn() {
		return inloggadUser==null ? null : inloggadUser.getKundNamn();
	}
	public String getAvtalsKundnr() {
		return inloggadUser==null ? StartupData.getDefaultKundnr() : inloggadUser.getKundnr();		
	}

	public KatalogGruppLista getKatalogGruppLista(Connection con) throws SQLException {
		if (kgl==null) kgl = SQLHandler.getKatalogGruppLista(con);
		return kgl;
	}

	public boolean isInkMoms() {
		return inkMoms;
	}

	public void setInkMoms(boolean inkMoms) {
		this.inkMoms = inkMoms;
	}
	
}
