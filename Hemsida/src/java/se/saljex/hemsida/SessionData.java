/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Ulf
 */
public class SessionData {
	private Varukorg varukorg = null;
	private User inloggadUser=null;
	
	KatalogGruppLista kgl=null;
	

	public User login(Connection con, String anvandarnamn, String losen) {
		inloggadUser=null;
		try {
			inloggadUser = SQLHandler.login(con, anvandarnamn, losen);
			if (inloggadUser!=null && varukorg!=null) varukorg.mergeSQLVarukorg(con, inloggadUser.getKontaktId());
		} catch (SQLException e) {}
		return inloggadUser;
	}
	
	public void logout() {
		varukorg=null;
		inloggadUser=null;
	}

	public Integer getLagerNr() {
		return inloggadUser!=null ? inloggadUser.getLagernr() : Const.getDefultLagernr();
	}

	
	public Varukorg getVarukorg(Connection con) {
		if (varukorg==null) {
			if (inloggadUser!=null) {
				try {
					varukorg = new Varukorg(con,inloggadUser.getKontaktId());
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
		return inloggadUser==null ? Const.getDefaultKundnr() : inloggadUser.getKundnr();		
	}

	public KatalogGruppLista getKatalogGruppLista(Connection con) throws SQLException {
		if (kgl==null) kgl = SQLHandler.getKatalogGruppLista(con);
		return kgl;
	}
	
}
