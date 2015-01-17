/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

/**
 *
 * @author Ulf
 */
public class User {
	Integer kontaktId;
	String kontaktNamn;
	String kontaktMail;
	String kundnr;
	String kundNamn;
	String kundSaljare;
	Integer lagernr;
	
	public Integer getKontaktId() {
		return kontaktId;
	}

	public void setKontaktId(Integer kontaktId) {
		this.kontaktId = kontaktId;
	}

	public String getKontaktNamn() {
		return kontaktNamn;
	}

	public void setKontaktNamn(String kontaktNamn) {
		this.kontaktNamn = kontaktNamn;
	}

	public String getKontaktMail() {
		return kontaktMail;
	}

	public void setKontaktMail(String kontaktMail) {
		this.kontaktMail = kontaktMail;
	}

	public String getKundnr() {
		return kundnr;
	}

	public void setKundnr(String kundnr) {
		this.kundnr = kundnr;
	}

	public String getKundNamn() {
		return kundNamn;
	}

	public void setKundNamn(String kundNamn) {
		this.kundNamn = kundNamn;
	}

	public String getKundSaljare() {
		return kundSaljare;
	}

	public void setKundSaljare(String kundSaljare) {
		this.kundSaljare = kundSaljare;
	}

	public Integer getLagernr() {
		return lagernr==null ? 0 : lagernr;
	}

	public void setLagernr(Integer lagernr) {
		this.lagernr = lagernr;
	}
	
		
	
}
