/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Array;

/**
 *
 * @author Ulf
 */
public class KatalogGrupp {
	private Integer grpId;
	private Integer prevGrpId;
	private Integer sortOrder;
	private String rubrik;
	private String text;
	private String html;
	private Integer depth;
	private Integer antalKlasar;
	private Integer[] sortPath;
	private Integer avdelning;
	public KatalogGrupp() {
	}

	
	public KatalogGrupp(Integer grpId, Integer prevGrpId, Integer sortorder, String rubrik, String text, String html, Integer depth, Integer antalKlasar, Array sortPath, Integer avdelning) {
		this.grpId = grpId;
		this.prevGrpId = prevGrpId;
		this.sortOrder = sortorder;
		this.rubrik = rubrik;
		this.text = text;
		this.html = html;
		this.depth = depth;
		this.antalKlasar = antalKlasar;
		try { this.sortPath = (Integer[])sortPath.getArray(); } catch (Exception e) {}
		this.avdelning = avdelning;
	}

	public Integer getAvdelning() {
		return avdelning;
	}

	public void setAvdelning(Integer avdelning) {
		this.avdelning = avdelning;
	}

	public Integer[] getSortPath() {
		return sortPath;
	}

	public void setSortPath(Integer[] sortPath) {
		this.sortPath = sortPath;
	}

	public Integer getAntalKlasar() {
		return antalKlasar;
	}

	public void setAntalKlasar(Integer antalKlasar) {
		this.antalKlasar = antalKlasar;
	}

	
	public Integer getGrpId() {
		return grpId;
	}

	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	public Integer getPrevGrpId() {
		return prevGrpId;
	}

	public void setPrevGrpId(Integer prevGrpId) {
		this.prevGrpId = prevGrpId;
	}

	public String getRubrik() {
		return rubrik;
	}

	public void setRubrik(String rubrik) {
		this.rubrik = rubrik;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

}
