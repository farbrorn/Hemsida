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
public class SokResultRow {
	private	Integer klasId;
	private	Integer grpId;
	private	String gruppRubrik;
	private	String klasRubrik;

	public Integer getKlasId() {
		return klasId;
	}

	public void setKlasId(Integer klasId) {
		this.klasId = klasId;
	}

	public Integer getGrpId() {
		return grpId;
	}

	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	public String getGruppRubrik() {
		return gruppRubrik;
	}

	public void setGruppRubrik(String gruppRubrik) {
		this.gruppRubrik = gruppRubrik;
	}

	public String getKlasRubrik() {
		return klasRubrik;
	}

	public void setKlasRubrik(String klasRubrik) {
		this.klasRubrik = klasRubrik;
	}
	
}
