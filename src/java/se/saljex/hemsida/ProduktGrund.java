/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.util.ArrayList;
import se.saljex.sxlibrary.SXUtil;


/**
 *
 * @author Ulf
 */
public class ProduktGrund {
    
	private Integer klasid;
    private String rubrik;
    private String kortBeskrivning;
    private String beskrivningHTML;
	private String autoBildArtnr;
	private String autoSamkoptaKlasar;
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

	public String getAutoBildArtnr() {
		return autoBildArtnr;
	}

	public void setAutoBildArtnr(String autoBildArtnr) {
		this.autoBildArtnr = autoBildArtnr;
	}

	public String getAutoSamkoptaKlasar() {
		return autoSamkoptaKlasar;
	}

	public ArrayList<Integer> getAutoSamkoptaKlasarAsArray() {
		if (SXUtil.isEmpty(autoSamkoptaKlasar)) return null;
		String[] split = autoSamkoptaKlasar.split(",");
		if (split==null || split.length < 1) return null;
		ArrayList<Integer> ret = new ArrayList<>();
		Integer i;
		for (String s : split) {
			try {
				i = new Integer(s);
				ret.add(i);
			} catch (Exception e) {}
		}
		if (ret.size() < 1) return null;
		return ret;
	}

	
	public void setAutoSamkoptaKlasar(String autoSamkoptaKlasar) {
		this.autoSamkoptaKlasar = autoSamkoptaKlasar;
	}
	
    
}
