/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ulf
 */
public class Artikel {
	private String artnr;
    private String namn;
    private String katNamn;
    private Double bruttopris;
    private String rsk;
	
    private Double nettoPris=null;

    private Double nettoPrisStaf1;
    private Double nettoPrisStaf2;
    private Double antalStaf1;
    private Double antalStaf2;
	
    private String enhet;
    private Integer levVillkor;
	private	Double antalSaljpack;
	private Integer antalSaljPackIForpack;
	private HashMap<Integer,LagerSaldo> lagerSaldon = new HashMap<>();

	public String getLagerSaldoString(Integer lagernr) {		
		LagerSaldo ls = lagerSaldon.get(lagernr);
		String r=null;
		if (ls==null) return "";
		Double ilager = ls.getTillgangliga()/getAntalSaljpackForDivision();
		if (ilager>=1000) r= "1000+ i lager";
		else if (ilager>=100) r= "100+ i lager";
		else if (ilager>=10) r= "10+ i lager";
		else if (ilager>=1) r= "Fåtal i lager";
		else if ((ls.getMaxlager()==null ? 0.0 : ls.getMaxlager()) <=0.0) r="Beställning";
		else r="Slut";
		return r;
	}
	
	public Double getAntalSaljpackForDivision() {
		Double a;
		a=getAntalSaljpack();
		if (a==null || a==0.0) a=1.0;
		return a;
	}
	public LagerSaldo getLagerSaldo(Integer lagernr) {
		
		LagerSaldo ls = lagerSaldon.get(lagernr);
		if (ls==null) {
			ls = new LagerSaldo();
			ls.setLagernr(lagernr);
			ls.setBest(0.0);
			ls.setIlager(0.0);
			ls.setIorder(0.0);
			ls.setLagernamn("");
			ls.setMaxlager(0.0);
		}
		return ls;
	}
	
	public HashMap<Integer, LagerSaldo> getLagerSaldon() {
		return lagerSaldon;
	}

	public void setLagerSaldon(HashMap<Integer, LagerSaldo> lagerSaldon) {
		this.lagerSaldon = lagerSaldon;
	}

	public void addLagerSaldoRow(LagerSaldo lagerSaldo) {
		lagerSaldon.put(lagerSaldo.getLagernr(), lagerSaldo);
	}
	
	public String getArtnr() {
		return artnr;
	}

	public void setArtnr(String artnr) {
		this.artnr = artnr;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public String getKatNamn() {
		return katNamn;
	}

	public void setKatNamn(String namn) {
		this.katNamn = namn;
	}

	public Double getBruttoPris() {
		return bruttopris;
	}

	public void setBruttoPris(Double pris) {
		this.bruttopris = pris;
	}

	public String getEnhet() {
		return enhet;
	}

	public void setEnhet(String enhet) {
		this.enhet = enhet;
	}

	public Integer getLevVillkor() {
		return levVillkor;
	}

	public void setLevVillkor(Integer levVillkor) {
		this.levVillkor = levVillkor;
	}

	public Double getAntalSaljpack() {
		return antalSaljpack;
	}

	public void setAntalSaljpack(Double antalSaljpack) {
		this.antalSaljpack = antalSaljpack;
	}

	public Integer getAntalSaljPackIForpack() {
		return antalSaljPackIForpack;
	}

	public void setAntalSaljPackIForpack(Integer antalSaljPackIForpack) {
		this.antalSaljPackIForpack = antalSaljPackIForpack;
	}
	public Double getNettoPris(boolean inkMoms) {
		return inkMoms ? nettoPris*Const.getStartupData().getMomsMultiplikator() : nettoPris;
	}
	public Double getNettoPrisExMoms() {
		return nettoPris;
	}

	public void setNettoPrisExMoms(Double nettoPris) {
		this.nettoPris = nettoPris;
	}

	public Double getNettoPrisStaf1ExMoms() {
		return nettoPrisStaf1;
	}
	public Double getNettoPrisStaf1(boolean inkMoms) {
		return inkMoms ? nettoPrisStaf1*Const.getStartupData().getMomsMultiplikator() : nettoPrisStaf1;
	}

	public void setNettoPrisStaf1ExMoms(Double nettoPrisStaf1) {
		this.nettoPrisStaf1 = nettoPrisStaf1;
	}
	public Double getNettoPrisStaf2(boolean inkMoms) {
		return inkMoms ? nettoPrisStaf2*Const.getStartupData().getMomsMultiplikator() : nettoPrisStaf2;
	}

	public Double getNettoPrisStaf2ExMoms() {
		return nettoPrisStaf2;
	}

	public void setNettoPrisStaf2ExMoms(Double nettoPrisStaf2) {
		this.nettoPrisStaf2 = nettoPrisStaf2;
	}

	public Double getAntalStaf1() {
		return antalStaf1;
	}

	public void setAntalStaf1(Double antalStaf1) {
		this.antalStaf1 = antalStaf1;
	}

	public Double getAntalStaf2() {
		return antalStaf2;
	}

	public void setAntalStaf2(Double antalStaf2) {
		this.antalStaf2 = antalStaf2;
	}
	
	public Double getNettoprisVidAntalSaljpack(int antal, boolean inkMoms) {
		Double antalSaljpack = getAntalSaljpack();
		if (antalSaljpack==null || antalSaljpack==0.0) antalSaljpack=1.0;
		if (getAntalStaf2()!=null && getNettoPrisStaf2ExMoms()!=null && getNettoPrisStaf2ExMoms()>0 && antal>=getAntalStaf2()/antalSaljpack) return getNettoPrisStaf2(inkMoms);
		if (getAntalStaf1()!=null && getNettoPrisStaf1ExMoms()!=null && getNettoPrisStaf1ExMoms()>0 && antal>=getAntalStaf1()/antalSaljpack) return getNettoPrisStaf1(inkMoms);
		else return getNettoPris(inkMoms);
	}
		

	public String getFormatEnhet() {
		return Const.getFormatEnhet(getEnhet());
	}
	
	public String getEnhetStringMedForpackning() {
		return getAntalSaljpack().equals(1.0) ? getFormatEnhet() : "x " + Const.getAnpassade2Decimaler(getAntalSaljpack()) + getFormatEnhet();
	}

	public String getRsk() {
		return rsk;
	}

	public void setRsk(String rsk) {
		this.rsk = rsk;
	}


   
}
