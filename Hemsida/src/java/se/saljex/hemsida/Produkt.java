/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulf
 */
public class Produkt {
    
	private Integer klasid;
    private String rubrik;
    private String kortBeskrivning;
    private String beskrivningHTML;
    private final List<Artikel> varianter = new ArrayList<>();
	private final List<Produkt> vanligaTillbehor = new ArrayList<>();
	private final List<Produkt> liknandeProdukter = new ArrayList<>();
	
    public String getRubrik() {
	return rubrik;
    }

	public Integer getKlasid() {
		return klasid;
	}

	public void setKlasid(Integer klasid) {
		this.klasid = klasid;
	}

    public void setRubrik(String rubrik) {
	this.rubrik = rubrik;
    }

    public String getKortBeskrivning() {
	return kortBeskrivning;
    }

    public void setBeskrivning(String kortBeskrivning) {
	this.kortBeskrivning = kortBeskrivning;
    }

	public String getBeskrivningHTML() {
		return beskrivningHTML;
	}

	public void setBeskrivningHTML(String beskrivningHTML) {
		this.beskrivningHTML = beskrivningHTML;
	}
    
    public List<Artikel> getVarianter() { return varianter; }
    
    public void addVariant(Artikel variant) {
		varianter.add(variant);
    }
	

	public List<Produkt> getVanligaTillbehor() { return vanligaTillbehor; }
	public void addVanligaTillbehor(Produkt produkt) { vanligaTillbehor.add(produkt); }
	public List<Produkt> getLiknandeProdukter() { return liknandeProdukter; }
	public void addLiknandeProdukter(Produkt produkt) { liknandeProdukter.add(produkt); }
	public Artikel getVariant(String artnr) {
		Artikel retur = null;
		for (Artikel a : varianter) {
			if (a.getArtnr() != null && a.getArtnr().equals(artnr)) {
				retur = a;
				break;
			}
		}
		return retur;
	}
}
