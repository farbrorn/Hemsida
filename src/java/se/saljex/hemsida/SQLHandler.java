/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.saljex.sxlibrary.SXUtil;

/**
 *
 * @author Ulf
 */
public class SQLHandler {

	public static String getSQLKatalogGrupper() {
		return " (with recursive kataloggrupper as ( "
				+ " select grpid, 0 as prevgrpid, rubrik, text, sortorder, html, 0 as depth , array[sortorder, grpid] as sortpath, grpid as avdelning from artgrp where prevgrpid = " + Const.getKatalogRootGrp()
				+ " union all select a.grpid, a.prevgrpid, a.rubrik, a.text, a.sortorder, a.html, depth+1 , sortpath || a.sortorder || a.grpid, avdelning from artgrp a "
				+ " join kataloggrupper  on kataloggrupper.grpid=a.prevgrpid) "
				+ " select * from kataloggrupper order by sortpath) ";
	}

	public static KatalogGruppLista getKatalogGruppLista(Connection con) throws SQLException {
		KatalogGruppLista kgl = new KatalogGruppLista();
		ArrayList<KatalogGrupp> avdelningar = null;
		if (Const.isFirstTradLevelAvdelning()) {
			avdelningar = new ArrayList<>();
		}
		kgl.setAvdelningar(avdelningar);
		String q = "select kgl.grpid, kgl.prevgrpid, kgl.rubrik, kgl.text, kgl.sortorder, kgl.html ,kgl.depth, kgl.sortpath, count(distinct agl.klasid), kgl.avdelning from " + getSQLKatalogGrupper() + " kgl "
				+ " left outer join artgrplank agl on agl.grpid=kgl.grpid "
				+ " group by kgl.grpid, kgl.prevgrpid, kgl.rubrik, kgl.text, kgl.sortorder, kgl.html ,kgl.depth, kgl.sortpath, kgl.avdelning order by kgl.sortpath";
		PreparedStatement ps = con.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		KatalogGrupp kg;
		Integer avdelning = null;
		while (rs.next()) {
			if (rs.getInt(7) == 0) {
				avdelning = rs.getInt(1);
			}
			kg = new KatalogGrupp(rs.getInt(1), rs.getInt(2), rs.getInt(5), rs.getString(3), rs.getString(4), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getArray(8), rs.getInt(10));
			kgl.addGrupp(kg);
			if (avdelningar != null && kg.getDepth() == 0) {
				avdelningar.add(kg);
			}
		}

		return kgl;
	}

	public static Produkt getProdukt(Connection con, Integer klasId) throws SQLException {
		if (klasId == null) {
			return null;
		}
		String q = "SELECT ak.rubrik, ak.text, ak.infourl, ak.fraktvillkor, ak.html FROM ARTKLASE ak " 
				+ " join artgrplank agl on agl.klasid = ak.klasid "
				+ " join ( " 
				+ getSQLKatalogGrupper() + ") ag on agl.grpid=ag.grpid where ak.klasid = ?";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, klasId);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		if (rs.next()) {
			p = new Produkt();
			p.setRubrik(rs.getString(1));
			p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString(5)) ? SXUtil.toHtml(rs.getString(2)) : rs.getString(5));
			p.setBeskrivning(rs.getString(2));
		}
		List<Artikel> pvl = getProduktVarianter(con, klasId);
		for (Artikel pv : pvl) {
			p.addVariant(pv);
		}

		return p;
	}

	public static ArrayList<Produkt> getProdukterCloseToID(Connection con, Integer klasId) throws SQLException {
		if (klasId == null) {
			klasId = 1;
		}
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "select * from (SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid > ? order by klasid limit 4) a  union "
				+ " select * from (SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid < ? order by klasid desc limit 4) b  order by klasid";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, klasId);
		ps.setInt(2, klasId);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		while (rs.next()) {
			p = new Produkt();
			p.setKlasid(rs.getInt(1));
			p.setRubrik(rs.getString(2));
			p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString(6)) ? SXUtil.toHtml(rs.getString(3)) : rs.getString(6));
			p.setBeskrivning(rs.getString(3));
			List<Artikel> pvl = getProduktVarianter(con, p.getKlasid());
			for (Artikel pv : pvl) {
				p.addVariant(pv);
			}
			produkter.add(p);
		}

		return produkter;
	}

	public static ArrayList<Produkt> getProdukterInGrupp(Connection con, Integer grpId) throws SQLException {
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "SELECT ak.klasid, ak.rubrik, ak.text, ak.infourl, ak.fraktvillkor, ak.html FROM ARTKLASE ak join artgrplank agl on agl.klasid=ak.klasid where agl.grpid=? order by agl.sortorder, ak.klasid ";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, grpId);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		while (rs.next()) {
			p = new Produkt();
			p.setKlasid(rs.getInt(1));
			p.setRubrik(rs.getString(2));
			p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString(6)) ? SXUtil.toHtml(rs.getString(3)) : rs.getString(6));
			p.setBeskrivning(rs.getString(3));
			List<Artikel> pvl = getProduktVarianter(con, p.getKlasid());
			for (Artikel pv : pvl) {
				p.addVariant(pv);
			}
			produkter.add(p);
		}

		return produkter;
	}

	public static String ARTIKEL_SELECT_LIST = " a.nummer, a.namn, a.katnamn, a.utpris, a.enhet, a.minsaljpack, a.forpack, a.fraktvillkor ";
	
	public static Artikel getArtikelFromSQL(ResultSet rs) throws SQLException {
		return getArtikelFromSQL(rs,0);
	}
	public static Artikel getArtikelFromSQL(ResultSet rs, int offset ) throws SQLException {
		Double forpack;
		Double minSaljPack;
		Double antalSaljPack;
		Integer antalSaljPackIForpack;
		Artikel pv = new Artikel();
		forpack = rs.getDouble(7+offset);
		minSaljPack = rs.getDouble(6+offset);
		if (forpack.compareTo(0.0) == 0) forpack=1.0;
		antalSaljPack = forpack;
		if (minSaljPack.compareTo(0.0) > 0) {
			antalSaljPack = minSaljPack;
		}
		if (antalSaljPack.compareTo(0.0) == 0) antalSaljPack = 1.0;
		if (forpack % antalSaljPack == 0 && forpack.compareTo(antalSaljPack) > 0) { //Om vi har jämt delbart förhållande mellan förpack och minsajpack
			antalSaljPackIForpack = ((Double)(forpack / antalSaljPack)).intValue();
		} else {
			antalSaljPackIForpack = 1;
		}
		pv.setAntalSaljpack(antalSaljPack);
		pv.setEnhet(rs.getString(5+offset));
		pv.setLevVillkor(rs.getInt(8+offset));
		pv.setPris(rs.getDouble(4+offset));
		pv.setKatNamn(rs.getString(3+offset));
		pv.setNamn(rs.getString(2+offset));
		pv.setArtnr(rs.getString(1+offset));
		pv.setAntalSaljPackIForpack(antalSaljPackIForpack);
		if (Const.isEmpty(pv.getKatNamn())) pv.setKatNamn(pv.getNamn());
		
		return pv;
	}

	public static Artikel getArtikel(Connection con, String artNr) throws SQLException {
		String q = "select " + ARTIKEL_SELECT_LIST +  " from artikel a where a.nummer=? ";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(1, artNr);
		ResultSet rs = ps.executeQuery();
		Artikel pv=null;
		if (rs.next()) {
			pv = getArtikelFromSQL(rs);
		}
		return pv;
	}
	
	public static VarukorgRow getArtikelProdukt(Connection con, String artNr, Integer klasid) throws SQLException {
		String q = "select ak.klasid, ak.rubrik, ak.text, ak.infourl, ak.fraktvillkor, ak.html, " + ARTIKEL_SELECT_LIST +  " from artikel a " 
				+ " join artklaselank akl on akl.artnr = a.nummer "
				+ " join artklase ak on ak.klasid=akl.klasid "
				+ " join artgrplank agl on agl.klasid = ak.klasid "
				+ " join ( " + getSQLKatalogGrupper() + ") ag on agl.grpid=ag.grpid " 
				+ " where a.nummer=? and akl.klasid=?";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(1, artNr);
		ps.setInt(2, klasid);
		ResultSet rs = ps.executeQuery();
		Artikel art=null;
		VarukorgRow vk = null;

		if (rs.next()) {
			vk = new VarukorgRow();
			art = getArtikelFromSQL(rs,6);
			Produkt p = new Produkt();
			p.setKlasid(rs.getInt(1));
			p.setRubrik(rs.getString(2));
			p.setBeskrivning(rs.getString(3));
			p.setBeskrivningHTML(rs.getString(6));
			
			vk.setArt(art);
			vk.setP(p);
		}
		
		return vk;
	}

	
	
	public static List<Artikel> getProduktVarianter(Connection con, Integer klasId) throws SQLException {
		List<Artikel> pvl = new ArrayList<>();
		String q = "select " + ARTIKEL_SELECT_LIST +  " from artklaselank akl join artikel a on akl.artnr=a.nummer where akl.klasid=? order by sortorder, artnr";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, klasId);
		ResultSet rs = ps.executeQuery();
		Artikel pv;
		while (rs.next()) {
			pv = getArtikelFromSQL(rs);
			pvl.add(pv);
		}

		return pvl;
	}

	public static ArrayList<Produkt> getLiknandeProdukterInGrp(Connection con, Integer klasId) throws SQLException {
		if (klasId == null) {
			klasId = 1;
		}
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid in "
				+ " (select  agl.klasid from artgrplank agl where agl.grpid in ( select grpid from artgrplank where klasid=? )) and klasid != ? "
				+ " order by klasid ";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, klasId);
		ps.setInt(2, klasId);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		while (rs.next()) {
			p = new Produkt();
			p.setKlasid(rs.getInt(1));
			p.setRubrik(rs.getString(2));
			p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString(6)) ? SXUtil.toHtml(rs.getString(3)) : rs.getString(6));
			p.setBeskrivning(rs.getString(3));
			List<Artikel> pvl = getProduktVarianter(con, p.getKlasid());
			for (Artikel pv : pvl) {
				p.addVariant(pv);
			}
			produkter.add(p);
		}

		return produkter;
	}

	public static SokResult sok(Connection con, String sokStr) throws SQLException {
		return sok(con, sokStr, 0, Const.getDefaultSokLimit());
	}
	public static SokResult sok(Connection con, String sokStr, int offset, int limit) throws SQLException {
		if (sokStr==null || sokStr.trim().length() <= 0) return null;
		
		String[] termer;
		termer = sokStr.split(" ");
		return termer.length>0 ? sok(con,termer, offset, limit) : null;
	}
	
	public static SokResult sok(Connection con, String[] sokTermer) throws SQLException{
		return sok(con, sokTermer,0,Const.getDefaultSokLimit());
	}
	public static SokResult sok(Connection con, String[] sokTermer, int offset, int limit) throws SQLException{
		/*
		 select grpid, ag_rubrik, ak_rubrik, klasid, sum(vikt), count(*) from (
		 SELECT terms.term, ag.grpid as grpid, ak.klasid as klasid, ag.rubrik as ag_rubrik, ak.rubrik as ak_rubrik, ak.text as ak_text,
		
		 case when upper(ag.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end +
		 case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then case when upper(ak.rubrik) like upper(terms.term)||'%' then 4 else 2 end else 0 end +
		 case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1 else 0 end  +
		 case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 6 else 0 end  +
		 case when string_agg('-'||a.refnr::text||'-'||a.enummer||'-'||a.rsk,' ') like '%-'||upper(terms.term)||'-%' then 4 else 0 end  
		 as vikt
		
		 from 
		 (with recursive kataloggrupper as ( 
		 select grpid, 0 as prevgrpid, rubrik, text, sortorder, html, 0 as depth , array[sortorder, grpid] as sortpath, grpid as avdelning from artgrp where prevgrpid = 0
		 union all select a.grpid, a.prevgrpid, a.rubrik, a.text, a.sortorder, a.html, depth+1 , sortpath || a.sortorder || a.grpid, avdelning from artgrp a 
		 join kataloggrupper  on kataloggrupper.grpid=a.prevgrpid) 
		 select * from kataloggrupper order by sortpath) ag

		 join artgrplank agl on ag.grpid=agl.grpid
		 join artklase ak on ak.klasid=agl.klasid
		 join  ( values('bbelnippel'), ('nipp')) as terms (term) on 1=1
		 left outer join (
		 select akl.klasid as klasid, a.nummer as nummer, a.refnr as refnr, a.rsk as rsk, a.enummer as enummer, a.katnamn as katnamn from artklaselank akl join artikel a on a.nummer = akl.artnr
		 ) a on a.klasid = ak.klasid and upper(coalesce(a.katnamn,'')::text||' '||coalesce(a.nummer,'')||' '||coalesce(a.refnr,'')||' '||coalesce(a.rsk,'')||' '||coalesce(a.enummer,'')) like  upper('%'||terms.term||'%')
		 group by ag.grpid, ak.klasid, ag.rubrik, ak.rubrik, ak.text, terms.term
		 having upper(concat(ag.rubrik , ak.rubrik , ak.text ,string_agg(coalesce(a.katnamn,'')::text||' '||a.nummer||' '||coalesce(a.refnr,'')||' '||coalesce(a.rsk,'')||' '||coalesce(a.enummer,''),' '))) like upper('%'||terms.term||'%')
		 ) s
		 group by grpid, ag_rubrik, ak_rubrik, klasid
		 having count(*) > 0
		 order by count(*) desc , sum(vikt) desc

		 */
		StringBuilder valueString = new StringBuilder();
		for (int i=0; i< sokTermer.length; i++ ) {
			if (i>0) valueString.append(",");
			valueString.append("(?)");
		}
		String q = 
				" select ak.klasid, ak.rubrik, ak.text, ak.infourl, ak.fraktvillkor, ak.html, "
				+ ARTIKEL_SELECT_LIST 
				+" from artklase ak  join artklaselank akl on akl.klasid=ak.klasid join artikel a on a.nummer=akl.artnr "
				+ " join "
				+ "	(select klasid as klasid, sum(vikt) as vikt, count(*) as antal from ("
				+ "		SELECT ak.klasid as klasid, terms.term as term, "

				+ "		case when upper(ag.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end +"
				+ "		case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then case when upper(ak.rubrik) like upper(terms.term)||'%' then 4 else 2 end else 0 end +"
				+ "		case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1 else 0 end  +"
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 6 else 0 end  +"
				+ "		case when string_agg('-'||a.refnr::text||'-'||a.enummer||'-'||a.rsk,' ') like '%-'||upper(terms.term)||'-%' then 4 else 0 end  "
				+ "		as vikt"

				+ "		from "
				+ " " + getSQLKatalogGrupper() + " ag"

				+ "		join artgrplank agl on ag.grpid=agl.grpid"
				+ "		join artklase ak on ak.klasid=agl.klasid"
				+ "		join  ( values" + valueString.toString() + " ) as terms (term) on 1=1"
				+ "		left outer join ("
				+ "			select akl.klasid as klasid, a.nummer as nummer, a.refnr as refnr, a.rsk as rsk, a.enummer as enummer, a.katnamn as katnamn from artklaselank akl join artikel a on a.nummer = akl.artnr"
				+ "		) a on a.klasid = ak.klasid and upper(coalesce(a.katnamn,'')::text||' '||coalesce(a.nummer,'')||' '||coalesce(a.refnr,'')||' '||coalesce(a.rsk,'')||' '||coalesce(a.enummer,'')) like  upper('%'||terms.term||'%')"
				+ "		group by ag.grpid, ak.klasid, ag.rubrik, ak.rubrik, ak.text, terms.term"
				+ "		having upper(concat(ag.rubrik , ak.rubrik , ak.text ,string_agg(coalesce(a.katnamn,'')::text||' '||a.nummer||' '||coalesce(a.refnr,'')||' '||coalesce(a.rsk,'')||' '||coalesce(a.enummer,''),' '))) like upper('%'||terms.term||'%')"
				+ "	) s"
				+ "	group by klasid"
				+ "	having count(distinct term) >= ?"
				+ "	offset ? limit ? ) klasar"
				
				+ " on klasar.klasid = akl.klasid "
				+ " order by klasar.antal desc , klasar.vikt desc, ak.rubrik, ak.klasid"
				
				;
		
		PreparedStatement ps = con.prepareStatement(q);
		int i = 0;
		for (String s: sokTermer) {
			i++;
			ps.setString(i, s);
		}
		i++;
		ps.setInt(i, sokTermer.length);
		i++;
		ps.setInt(i, offset);
		i++;
		ps.setInt(i, limit);
		ResultSet rs = ps.executeQuery();
		SokResult sokResultat = new SokResult();
		SokResultRow row;
		Produkt p=null;
		Artikel pv=null;
		
		Double forpack;
		Double minSaljPack;
		Double antalSaljPack;
		
		while (rs.next()) {
			if (p==null || !p.getKlasid().equals(rs.getInt(1))) {
				p = new Produkt();
				sokResultat.getPl().addProdukt(p);
				p.setKlasid(rs.getInt(1));
				p.setRubrik(rs.getString(2));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString(6)) ? SXUtil.toHtml(rs.getString(3)) : rs.getString(6));
				p.setBeskrivning(rs.getString(3));
			}
			
			pv = getArtikelFromSQL(rs,6);
			p.getVarianter().add(pv);
			
		}
		return sokResultat;
	}
	
	
	public static User login(Connection con, String anvandarnamn, String losen) throws SQLException{
		User u = null;
		if (anvandarnamn!=null && losen!=null) {
			String q = 
					"select kk.namn, kk.epost, kk.kontaktid, k.nummer, k.namn " +
					" from kund k join kundkontakt kk on kk.kundnr=k.nummer join kundlogin kl on kl.kontaktid=kk.kontaktid " +
					" where kl.loginnamn=? and kl.loginlosen=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, anvandarnamn);
			ps.setString(2, losen);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.setKontaktId(rs.getInt(3));
				u.setKontaktMail(rs.getString(2));
				u.setKontaktNamn(rs.getString(1));
				u.setKundNamn(rs.getString(5));
				u.setKundnr(rs.getString(4));
			}
		}
		return u;
	}
	
}
