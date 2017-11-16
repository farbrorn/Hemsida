/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import se.saljex.sxlibrary.exceptions.KreditSparrException;

/**
 *
 * @author Ulf
 */
public class Kund {
	private String kundnr;
	private String namn;
	private String adr1;
	private String adr2;
	private String adr3;
	private String lnamn;
	private String ladr2;
	private String ladr3;
	private String saljare;
	private String tel;
	private String biltel;
	private String regnr;
	private Double kgrans;
	private Integer ktid;
	private String nettolst;
	private Integer bonus;
	private Boolean mottagarfrakt;
	private String fraktbolag;
	private Double fraktfrigrans;
	private Integer distrikt;
	private boolean kravordermarke;
	private String linjenr1;
	private String linjenr2;
	private String linjenr3;
	private boolean skickafakturaepost;
	private Double reskontra;
	private Double reskontraForfall30;
	private Double reskontraKreditEjForfallen30;

	public String getKundnr() {
		return kundnr;
	}

	public void setKundnr(String kundnr) {
		this.kundnr = kundnr;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public String getAdr1() {
		return adr1;
	}

	public void setAdr1(String adr1) {
		this.adr1 = adr1;
	}

	public String getAdr2() {
		return adr2;
	}

	public void setAdr2(String adr2) {
		this.adr2 = adr2;
	}

	public String getAdr3() {
		return adr3;
	}

	public void setAdr3(String adr3) {
		this.adr3 = adr3;
	}

	public String getLnamn() {
		return lnamn;
	}

	public void setLnamn(String lnamn) {
		this.lnamn = lnamn;
	}

	public String getLadr2() {
		return ladr2;
	}

	public void setLadr2(String ladr2) {
		this.ladr2 = ladr2;
	}

	public String getLadr3() {
		return ladr3;
	}

	public void setLadr3(String ladr3) {
		this.ladr3 = ladr3;
	}

	public String getSaljare() {
		return saljare;
	}

	public void setSaljare(String saljare) {
		this.saljare = saljare;
	}

	public String getBiltel() {
		return biltel;
	}

	public void setBiltel(String biltel) {
		this.biltel = biltel;
	}

	public String getRegnr() {
		return regnr;
	}

	public void setRegnr(String regnr) {
		this.regnr = regnr;
	}

	public Double getKgrans() {
		return kgrans==null ? 0.0 : kgrans;
	}

	public void setKgrans(Double kgrans) {
		this.kgrans = kgrans;
	}

	public Integer getKtid() {
		return ktid;
	}

	public void setKtid(Integer ktid) {
		this.ktid = ktid;
	}

	public String getNettolst() {
		return nettolst;
	}

	public void setNettolst(String nettolst) {
		this.nettolst = nettolst;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public Boolean getMottagarfrakt() {
		return mottagarfrakt;
	}

	public void setMottagarfrakt(Boolean mottagarfrakt) {
		this.mottagarfrakt = mottagarfrakt;
	}

	public String getFraktbolag() {
		return fraktbolag;
	}

	public void setFraktbolag(String fraktbolag) {
		this.fraktbolag = fraktbolag;
	}

	public Double getFraktfrigrans() {
		return fraktfrigrans;
	}

	public void setFraktfrigrans(Double fraktfrigrans) {
		this.fraktfrigrans = fraktfrigrans;
	}

	public Integer getDistrikt() {
		return distrikt;
	}

	public void setDistrikt(Integer distrikt) {
		this.distrikt = distrikt;
	}

	public boolean isKravordermarke() {
		return kravordermarke;
	}

	public void setKravordermarke(boolean kravordermarke) {
		this.kravordermarke = kravordermarke;
	}

	public String getLinjenr1() {
		return linjenr1;
	}

	public void setLinjenr1(String linjenr1) {
		this.linjenr1 = linjenr1;
	}

	public String getLinjenr2() {
		return linjenr2;
	}

	public void setLinjenr2(String linjenr2) {
		this.linjenr2 = linjenr2;
	}

	public String getLinjenr3() {
		return linjenr3;
	}

	public void setLinjenr3(String linjenr3) {
		this.linjenr3 = linjenr3;
	}

	public boolean isSkickafakturaepost() {
		return skickafakturaepost;
	}

	public void setSkickafakturaepost(boolean skickafakturaepost) {
		this.skickafakturaepost = skickafakturaepost;
	}

	public Double getReskontra() {
		return reskontra==null ? 0.0 : reskontra;
	}

	public void setReskontra(Double reskontra) {
		this.reskontra = reskontra;
	}

	public Double getReskontraForfall30() {
		return reskontraForfall30==null ? 0.0 : reskontraForfall30;
	}

	public void setReskontraForfall30(Double reskontraForfall30) {
		this.reskontraForfall30 = reskontraForfall30;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Double getReskontraKreditEjForfallen30() {
		return reskontraKreditEjForfallen30;
	}

	public void setReskontraKreditEjForfallen30(Double reskontraKreditEjForfallen30) {
		this.reskontraKreditEjForfallen30 = reskontraKreditEjForfallen30;
	}

	public boolean isKreditgransUppnadd() {
		if (isKredigransForfallet30Uppnadd()) return true;
		
		if (getKgrans().equals(0.0) ) return false;
		return getReskontra().compareTo(getKgrans())>0;
	}
	
	public boolean isKredigransForfallet30Uppnadd() {
		return (getReskontraForfall30() + getReskontraKreditEjForfallen30())>0;
	}

}
