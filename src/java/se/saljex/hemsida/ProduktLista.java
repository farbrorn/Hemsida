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
public class ProduktLista {
	private Integer gruppId;
	private String namn;
	private final List<Produkt> produkter = new ArrayList<>();
	private final List<Produkt> framhavdaProdukter = new ArrayList<>();

	public Integer getGruppId() {
		return gruppId;
	}

	public void setGruppId(Integer gruppId) {
		this.gruppId = gruppId;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}


	public List<Produkt> getProdukter() {
		return produkter;
	}
	
	public void addProdukt(Produkt produkt) {
		produkter.add(produkt);
	} 
	public List<Produkt> getFramhavdaProdukter() {
		return framhavdaProdukter;
	}
	
	public void addFramhavdaProdukt(Produkt produkt) {
		framhavdaProdukter.add(produkt);
	} 
}
