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
public class Artikel {
	private String artnr;
    private String namn;
    private String katNamn;
    private Double pris;
    private String enhet;
    private Integer levVillkor;
	private	Double antalSaljpack;
	private Integer antalSaljPackIForpack;
	

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

	public Double getPris() {
		return pris;
	}

	public void setPris(Double pris) {
		this.pris = pris;
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
	
		
		


   
}
