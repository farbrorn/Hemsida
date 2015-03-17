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
public class Page {
	public static final String STATUS_SIDA="sida";
	public static final String STATUS_SIDSEGMENT="segment";
	public static final String STATUS_RADERAD="raderad";
	public static final String STATUS_CARD_LEFT_TOP="card-left-top";
	public static final String STATUS_CARD_LEFT_BOT="card-left-bot";
	public static final String STATUS_CARD_MID_TOP="card-mid-top";
	public static final String STATUS_CARD_MID_BOT="card-mid-bot";
	public static final String STATUS_CARD_RIGHT_TOP="card-right-top";
	public static final String STATUS_CARD_RIGHT_BOT="card-right-bot";
	
	
	String sidId=null;
	String status=null;
	String rubrik=null;
	String html=null;

	public String getSidId() {
		return sidId;
	}

	public void setSidId(String sidId) {
		this.sidId = sidId;
	}

	public String getRubrik() {
		return rubrik;
	}

	public void setRubrik(String rubrik) {
		this.rubrik = rubrik;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
