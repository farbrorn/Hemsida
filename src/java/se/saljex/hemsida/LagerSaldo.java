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
public class LagerSaldo {
	Integer lagernr;
	Double iorder;
	Double ilager;
	Double best;
	Double maxlager;
	String lagernamn;

	public Double getMaxlager() {
		return maxlager;
	}

	public void setMaxlager(Double maxlager) {
		this.maxlager = maxlager;
	}

	
	public Double getTillgangliga() { return ilager-iorder; }
	public Integer getLagernr() {
		return lagernr;
	}

	public void setLagernr(Integer lagernr) {
		this.lagernr = lagernr;
	}

	public Double getIorder() {
		return iorder;
	}

	public void setIorder(Double iorder) {
		this.iorder = iorder;
	}

	public Double getIlager() {
		return ilager;
	}

	public void setIlager(Double ilager) {
		this.ilager = ilager;
	}

	public Double getBest() {
		return best;
	}

	public void setBest(Double best) {
		this.best = best;
	}

	public String getLagernamn() {
		return lagernamn;
	}

	public void setLagernamn(String lagernamn) {
		this.lagernamn = lagernamn;
	}
	
}
