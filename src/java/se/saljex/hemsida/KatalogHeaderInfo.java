/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;

/**
 *
 * @author Ulf
 */
public class KatalogHeaderInfo {
	private KatalogGrupp katalogGrupp;
	private ArrayList<KatalogGrupp> children = new ArrayList<>();
	private ArrayList<KatalogGrupp> sokvag = new ArrayList<>();
	private KatalogGrupp nextGrp;
	private KatalogGrupp prevGrp;

	public KatalogGrupp getKatalogGrupp() {
		return katalogGrupp;
	}

	public void setKatalogGrupp(KatalogGrupp katalogGrupp) {
		this.katalogGrupp = katalogGrupp;
	}

	public ArrayList<KatalogGrupp> getSokvag() {
		return sokvag;
	}

	public void setSokvag(ArrayList<KatalogGrupp> sokvag) {
		this.sokvag = sokvag;
	}

	public ArrayList<KatalogGrupp> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<KatalogGrupp> children) {
		this.children = children;
	}

	public KatalogGrupp getNextGrp() {
		return nextGrp;
	}

	public void setNextGrp(KatalogGrupp nextGrp) {
		this.nextGrp = nextGrp;
	}

	public KatalogGrupp getPrevGrp() {
		return prevGrp;
	}

	public void setPrevGrp(KatalogGrupp prevGrp) {
		this.prevGrp = prevGrp;
	}
	
}
