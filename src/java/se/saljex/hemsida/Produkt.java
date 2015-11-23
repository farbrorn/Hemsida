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
public class Produkt extends ProduktGrund{
    
    private final List<Artikel> varianter = new ArrayList<>();
	private final List<Produkt> vanligaTillbehor = new ArrayList<>();
	private final List<Produkt> liknandeProdukter = new ArrayList<>();

	public Produkt() {
		super();
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
	public Artikel getLagstaPrisArtikel() {
		Artikel returnArtikel = null;
		for (Artikel a : varianter) {
			if (returnArtikel==null) returnArtikel=a;
			else if ( a.getNettoPris(false).compareTo(returnArtikel.getNettoPris(false)) < 0) returnArtikel = a; 
		}
		return returnArtikel;
	}
	
}
