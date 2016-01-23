/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	private LagerEnhet lagerEnhet=null;
	
	
	public User login(Connection con, HttpServletRequest request, String anvandarnamn, String losen) {
		inloggadUser=null;
		try {
			inloggadUser = SQLHandler.login(con, anvandarnamn, losen);
			if (inloggadUser!=null && varukorg!=null) varukorg.mergeSQLVarukorg(request);
		} catch (SQLException e) {}
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
					if (inloggadUser!=null) {
						PreparedStatement ps = con.prepareStatement("update butikautologin set expiredate=current_date+30 where uuid=?");
						ps.setString(1, autoLoginId);
						ps.executeUpdate();
						if (varukorg!=null) varukorg.mergeSQLVarukorg(request);
						
					}
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

						
						u = con.prepareStatement("insert into butikautologin (uuid, kontaktid, expiredate) values (?,?,current_date+30)");
						u.setString(1, autoLoginId);
						u.setInt(2, inloggadUser.getKontaktId());
						u.executeUpdate();
						
						Cookie c2 = new Cookie(Const.COOKIEAUTOINLOGID, autoLoginId);
						c2.setMaxAge(365*24*60*60);
						response.addCookie(c2);
			} catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	public void logout(Connection con ) {
		try { SQLHandler.logoutAutoLogin(con, inloggadUser); } catch (SQLException e) { e.printStackTrace(); }
		varukorg=null;
		inloggadUser=null;
//		inkMoms=true;
	}

	public Integer getLagerNr() {
		if (inloggadUser!=null) {
			return inloggadUser.getDefultLagernr();
		} else {
			return lagerEnhet==null?  StartupData.getDefultLagernr() : lagerEnhet.getLagernr();
		}
	}

	public void setLager(HttpServletRequest request) {
		if (inloggadUser!=null) {
			setLager(request,inloggadUser.getDefultLagernr());
		} else {
			Integer lagernr = Const.getInitData(request).getDataCookie().getLagernr();
			if (lagernr==null) lagernr = StartupData.getDefultLagernr();
			setLager(request, lagernr);			
		}
	}
	public LagerEnhet getLager() {
		return lagerEnhet;
	}
	
	public void setLager(HttpServletRequest request, Integer lagernr) { 
		StartupData sData = Const.getStartupData();
		LagerEnhet le = sData.getLagerEnhet(lagernr);
		if (inloggadUser!=null) {
			this.lagerEnhet = le;
			inloggadUser.setDefaultLagernr(lagernr);
		} else {
			this.lagerEnhet = le;			
			Const.getInitData(request).getDataCookie().setLagernr(lagernr);			
		}
	}

	public String getLagerNamn() {
		return lagerEnhet==null ? getLagerNr().toString() : lagerEnhet.getNamn();
	}

	
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
		return Const.getStartupData().getKatalogGruppLista();
	}

	public boolean isInkMoms(HttpServletRequest request) {
		Boolean inkmoms;
		if (inloggadUser!=null)  inkmoms = inloggadUser.isDefaultInkMoms(); 
		else {
			inkmoms = Const.getInitData(request).getDataCookie().getInkmoms();
			if (inkmoms==null) inkmoms = true;
		}
		return inkmoms;
	}

	public void setInkMoms(HttpServletRequest request, boolean inkMoms) {
		if (inloggadUser!=null) {
			inloggadUser.setDefaultInkMoms(inkMoms);
		} else {
			Const.getInitData(request).getDataCookie().setInkmoms(inkMoms);
		}
	}
	
}
