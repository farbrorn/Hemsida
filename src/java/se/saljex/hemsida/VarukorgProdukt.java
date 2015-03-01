/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;
import java.util.List;
import sun.nio.cs.HistoricallyNamedCharset;

/**
 *
 * @author Ulf
 */
public class VarukorgProdukt {
	List<VarukorgArtikel> artiklar = new ArrayList<>();
	Produkt produkt;
	
	public void setProdukt(Produkt produkt) {this.produkt = produkt; }
	public VarukorgArtikel addArtikel(Artikel artikel, Integer antal) {
		VarukorgArtikel a = new VarukorgArtikel();
		a.setArt(artikel);
		a.setAntal(antal);
		artiklar.add(a);
		return a;
	}
	public VarukorgArtikel addArtikel(Artikel artikel, Integer antal, String htmlFormData, String htmlErrorMEssage) {
		VarukorgArtikel a = new VarukorgArtikel();
		a.setArt(artikel);
		a.setAntal(antal);
		a.setHtmlFormErrorMessage(htmlErrorMEssage);
		a.setHtmlFormValue(htmlFormData);
		artiklar.add(a);
		return a;
	}
	
	public List<VarukorgArtikel> getVarukorgArtiklar() {
		return artiklar;
	}
	
	public Produkt getProdukt() {
		return produkt;
	}
	
	public VarukorgArtikel getVariant(String artnr) {
		VarukorgArtikel row = null;
		if (artnr==null) return null;
		for (VarukorgArtikel a : artiklar) {
			if (a.getArt() != null) {
				if (artnr.equals(a.getArtnr()) ) {
					row = a;
					break;
				}
			}
		}
		return row;
	}
	
	
}
