/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ulf
 */
public class VarukorgFormHandler {
	
	public static final String PARAMETER_NAME="varukorgformhandler";
	
	List<VarukorgProdukt> rows;
	private boolean formHasErrors=false;
	
	public static final String FORMNAME_KID = "vkf-kid-";
	public static final String FORMNAME_AID = "vkf-aid-";
	public static final String FORMNAME_ANTAL = "vkf-antal-";

	public static final String FORMNAME_KUNDNR = "kundnr";
	public static final String FORMNAME_FORETAG = "foretag";
	public static final String FORMNAME_KONTAKTPERSON = "kontaktperson";
	public static final String FORMNAME_ADRESS = "adress";
	public static final String FORMNAME_POSTNR = "postnr";
	public static final String FORMNAME_ORT = "ort";
	public static final String FORMNAME_TEL = "tel";
	public static final String FORMNAME_ORGNR = "orgnr";
	public static final String FORMNAME_MARKE = "marke";
	public static final String FORMNAME_EPOST = "epost";
	public static final String FORMNAME_MEDDELANDE = "meddelande";
	
	private String kundnr;
	private String foretag;
	private String kontaktperson;
	private String adress;
	private String postnr;
	private String ort;
	private String tel;
	private String orgnr;
	private String marke;
	private String epost;
	private String meddelande;

	private String epostErrorMsg;
	private String recaptchaErrorMsg;

	public boolean hasFormError() {return formHasErrors; }
	public List<VarukorgProdukt> getRows() { return rows; }

	public VarukorgFormHandler(List<VarukorgProdukt> rows) {
		setRows(rows);
	}
	public VarukorgFormHandler(HttpServletRequest request) throws SQLException{
		parseForm(request);
	}
	
	
	
	
	public void setRows(List<VarukorgProdukt> rows) {
		formHasErrors=false;
		this.rows=rows;
	}
	
	public void parseForm(HttpServletRequest request) throws SQLException{
		formHasErrors=false;
		kundnr = request.getParameter(FORMNAME_KUNDNR);
		foretag = request.getParameter(FORMNAME_FORETAG);
		kontaktperson = request.getParameter(FORMNAME_KONTAKTPERSON);
		adress = request.getParameter(FORMNAME_ADRESS);
		postnr = request.getParameter(FORMNAME_POSTNR);
		ort = request.getParameter(FORMNAME_ORT);
		tel = request.getParameter(FORMNAME_TEL);
		orgnr = request.getParameter(FORMNAME_ORGNR);
		marke = request.getParameter(FORMNAME_MARKE);
		epost=  request.getParameter(FORMNAME_EPOST);
		meddelande=  request.getParameter(FORMNAME_MEDDELANDE);
		
		if (epost==null || epost.length() < 6) {
			formHasErrors = true;
			epostErrorMsg = "E-Post måste anges";
		}
		if (Const.getSessionData(request).getInloggadUser()==null) {
			if (!ReCaptcha.check(request)) {
				formHasErrors = true;
				recaptchaErrorMsg = "Du måste kryssa i rutan";
			}
		}
		
		
		rows = new ArrayList<>();
		Integer kid;
		String aid;
		Integer antal;
		VarukorgProdukt vp;
		String formAntal;
		for (int cn=0; cn<100000; cn++) {
			aid=null;
			kid=null;
			antal=null;
			aid = request.getParameter(FORMNAME_AID + cn);
			try { kid = new Integer(request.getParameter(FORMNAME_KID+ cn)); } catch (Exception e) {}
			formAntal = request.getParameter(FORMNAME_ANTAL+ cn);
			try { antal = new Integer(formAntal); } catch (Exception e) {}
			if (kid != null && aid != null) {
				VarukorgRow vr = SQLHandler.getArtikelProdukt(Const.getConnection(request), aid, kid, Const.getSessionData(request).getAvtalsKundnr(), Const.getSessionData(request).getLagerNr());
				if (antal==null) formHasErrors=true;
				if (vr!=null) {
					vp = getOrAddRow(vr.getP());
					vp.addArtikel(vr.getArt(), antal, formAntal, antal==null ? "Felaktigt antal" : null);
				}
			}
		} 
			
	}
	private VarukorgProdukt getOrAddRow(Produkt produkt) {
		for (VarukorgProdukt p : rows) {
			if (p.getProdukt().getKlasid().equals(produkt.getKlasid())) return p;
		}
		VarukorgProdukt vp = new VarukorgProdukt();
		vp.setProdukt(produkt);
		rows.add(vp);
		return vp;
	}

	public boolean isFormHasErrors() {
		return formHasErrors;
	}

	public void setFormHasErrors(boolean formHasErrors) {
		this.formHasErrors = formHasErrors;
	}

	public String getKundnr() {
		return kundnr;
	}

	public void setKundnr(String kundnr) {
		this.kundnr = kundnr;
	}

	public String getForetag() {
		return foretag;
	}

	public void setForetag(String foretag) {
		this.foretag = foretag;
	}

	public String getKontaktperson() {
		return kontaktperson;
	}

	public void setKontaktperson(String kontaktperson) {
		this.kontaktperson = kontaktperson;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPostnr() {
		return postnr;
	}

	public void setPostnr(String postnr) {
		this.postnr = postnr;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getOrgnr() {
		return orgnr;
	}

	public void setOrgnr(String orgnr) {
		this.orgnr = orgnr;
	}

	public String getMarke() {
		return marke;
	}

	public void setMarke(String marke) {
		this.marke = marke;
	}

	public String getMeddelande() {
		return meddelande;
	}

	public void setMeddelande(String meddelande) {
		this.meddelande = meddelande;
	}

	public String getEpost() {
		return epost;
	}

	public void setEpost(String epost) {
		this.epost = epost;
	}



	public String getEpostErrorMsg() {
		return epostErrorMsg;
	}

	public String getRecaptchaErrorMsg() {
		return recaptchaErrorMsg;
	}


	
	
	
}
