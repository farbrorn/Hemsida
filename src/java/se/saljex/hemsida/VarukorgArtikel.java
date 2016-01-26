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
public class VarukorgArtikel {
	Artikel art;
	Integer antal;
	
	String htmlFormValue=null;
	String htmlFormErrorMessage=null;

	public Artikel getArt() {
		return art;
	}

	public void setArt(Artikel art) {
		this.art = art;
	}

	public Integer getAntal() {
		return antal;
	}

	public void setAntal(Integer antal) {
		this.antal = antal;
	}
	public String getArtnr() {
		return art.getArtnr();
	}
	public String getKatnamn() {
		return art.getKatNamn();
	}
	public void addToAntal(Integer antal) {
		if (this.antal==null) this.antal=0;
		this.antal += antal;
	}
	public String getFormatEnhet() {
		return Const.getFormatEnhet(art.getEnhet());
	}
	public String _dep_getAnpassatPris() {
		return Const.getAnpassatPrisFormat(art.getBruttoPris());
	}

	public String getHtmlFormValue() {
		return htmlFormValue;
	}

	public void setHtmlFormValue(String htmlFormValue) {
		this.htmlFormValue = htmlFormValue;
	}

	public String getHtmlFormErrorMessage() {
		return htmlFormErrorMessage;
	}

	public void setHtmlFormErrorMessage(String htmlFormErrorMessage) {
		this.htmlFormErrorMessage = htmlFormErrorMessage;
	}
	public LagerSaldo getSQLLookupLagerSaldo(Connection con, int lagernr) {
		LagerSaldo ls = null;
		try {
			ls = SQLHandler.getLagerSaldo(con, getArtnr(), lagernr);
		} catch(SQLException e) { e.printStackTrace(); }
		
		return ls;
	}
	
}
