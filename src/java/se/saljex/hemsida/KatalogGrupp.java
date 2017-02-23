/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	private boolean visaundergrupper;
	private String htmlHead;
	private String htmlFoot;
	private ArrayList<Produkt> topProdukter = null;
	private ArrayList<Produkt> topRekommenderadeProdukter = null;
	public KatalogGrupp() {
	}

	
	public KatalogGrupp(Integer grpId, Integer prevGrpId, Integer sortorder, String rubrik, String text, String html, Integer depth, Integer antalKlasar, Array sortPath, Integer avdelning, String htmlHead, String htmlFoot, boolean visaundergrupper) {
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
		this.htmlHead = htmlHead;
		this.htmlFoot = htmlFoot;
		this.visaundergrupper = visaundergrupper;
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
	
	public List<Produkt> getTopProdukter(Connection con) {
		if (topProdukter == null) {
			try {
				topProdukter = SQLHandler.getToplistaInGrupp(con, getGrpId(), StartupData.getDefaultKundnr(), StartupData.getDefultLagernr(), 4);
			} catch (SQLException e) {
				Const.log("SQL: Fel vid getTopBilder. " + e.getMessage());
			}
			
		}
		return topProdukter;
	}
	public List<Produkt> getTopRekommenderadeProdukter(Connection con) {
		if (topRekommenderadeProdukter == null) {
			try {
				topRekommenderadeProdukter = SQLHandler.getRekommenderadeToplistaInGrupp(con, getGrpId(), StartupData.getDefaultKundnr(), StartupData.getDefultLagernr(), 4);
			} catch (SQLException e) {
				Const.log("SQL: Fel vid getTopBilder. " + e.getMessage());
			}
			
		}
		return topRekommenderadeProdukter;
	}

	public boolean isVisaundergrupper() {
		return visaundergrupper;
	}

	public void setVisaundergrupper(boolean visaundergrupper) {
		this.visaundergrupper = visaundergrupper;
	}

	public String getHtmlHead() {
		return htmlHead;
	}

	public void setHtmlHead(String htmlHead) {
		this.htmlHead = htmlHead;
	}

	public String getHtmlFoot() {
		return htmlFoot;
	}

	public void setHtmlFoot(String htmlFoot) {
		this.htmlFoot = htmlFoot;
	}

}
