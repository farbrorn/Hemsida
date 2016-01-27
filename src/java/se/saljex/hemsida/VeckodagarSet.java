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
public class VeckodagarSet {
	private boolean mandag=false;
	private boolean tisdag=false;
	private boolean onsdag=false;
	private boolean torsdag=false;
	private boolean fredag=false;
	private boolean lordag=false;
	private boolean sondag=false;
	

	public boolean isMandag() {
		return mandag;
	}

	public void setMandag(boolean mandag) {
		this.mandag = mandag;
	}

	public boolean isTisdag() {
		return tisdag;
	}

	public void setTisdag(boolean tisdag) {
		this.tisdag = tisdag;
	}

	public boolean isOnsdag() {
		return onsdag;
	}

	public void setOnsdag(boolean onsdag) {
		this.onsdag = onsdag;
	}

	public boolean isTorsdag() {
		return torsdag;
	}

	public void setTorsdag(boolean torsdag) {
		this.torsdag = torsdag;
	}

	public boolean isFredag() {
		return fredag;
	}

	public void setFredag(boolean fredag) {
		this.fredag = fredag;
	}

	public boolean isLordag() {
		return lordag;
	}

	public void setLordag(boolean lordag) {
		this.lordag = lordag;
	}

	public boolean isSondag() {
		return sondag;
	}

	public void setSondag(boolean sondag) {
		this.sondag = sondag;
	}

	public boolean isAnyDay() {
		return mandag || tisdag || onsdag || torsdag || fredag || lordag || sondag;
	}
	
}
