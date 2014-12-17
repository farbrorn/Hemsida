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
public class VarukorgArtikel {
	Artikel art;
	Integer antal;

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
	public String getAnpassatPris() {
		return Const.getAnpassatPrisFormat(art.getPris());
	}
	
}
