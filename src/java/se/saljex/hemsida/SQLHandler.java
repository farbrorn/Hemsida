/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import se.saljex.sxlibrary.SXUtil;

/**
 *
 * @author Ulf
 */
public class SQLHandler {
		public static final String V_SELECT_COLS = " v.ak_klasid, v.ak_rubrik, v.ak_text, v.ak_html, v.nummer, v.namn, v.katnamn, "
				+ " v.utpris, v.enhet, v.minsaljpack, v.forpack, v.fraktvillkor, v.lid_lagernr, v.l_ilager, v.l_maxlager, v.l_best, v.l_iorder, v.lid_bnamn, "
				+ " v.kundnetto_bas, v.kundnetto_staf1, v.kundnetto_staf2, v.staf_antal1, v.staf_antal2, v.rsk, v.refnr, v.ak_auto_samkopta_klasar, v.staf_pris1, v.staf_pris2, v.bildartnr as bildartnr , v.ak_auto_bildartnr as auto_bildartnr, v.ak_webbeskrivningfrangrpid as ak_webbeskrivningfrangrpid ";

	public static String getSQLKatalogGrupper(Integer rootGrp, boolean includeRootGrp, boolean kortSelectsats) {
		String s = StartupData.getKatalogExcludeGrpAsString();
		String st;
		if (includeRootGrp) st = "grpid= " + rootGrp;
		else st="prevgrpid = " + rootGrp;

		if (!kortSelectsats)
		return " (with recursive kataloggrupper as ( "
				+ " select grpid, 0 as prevgrpid, rubrik, text, sortorder, html, 0 as depth , array[ rubrik, sortorder::varchar, grpid::varchar] as sortpath, grpid as avdelning, array[grpid] as path, htmlhead, htmlfoot, visaundergrupper from artgrp where " + st + " and grpid not in (" + s + ")" 
				+ " union all select a.grpid, a.prevgrpid, a.rubrik, a.text, a.sortorder, a.html, depth+1 , sortpath || a.rubrik || a.sortorder::varchar || a.grpid::varchar, avdelning, path || a.grpid, a.htmlhead, a.htmlfoot, a.visaundergrupper from artgrp a  "
				+ " join kataloggrupper  on kataloggrupper.grpid=a.prevgrpid and kataloggrupper.grpid not in (" + s + ")) "
				+ " select * from kataloggrupper order by sortpath) ";
		else
		return " (with recursive kataloggrupper as ( "
				+ " select grpid, 0 as prevgrpid, rubrik, text, sortorder, html, htmlhead, htmlfoot, visaundergrupper  from artgrp where " + st + " and grpid not in (" + s + ")" 
				+ " union all select a.grpid, a.prevgrpid, a.rubrik, a.text, a.sortorder, a.html, a.htmlhead, a.htmlfoot, a.visaundergrupper  from artgrp a  "
				+ " join kataloggrupper  on kataloggrupper.grpid=a.prevgrpid and kataloggrupper.grpid not in (" + s + ")) "
				+ " select * from kataloggrupper) ";
			
/*		return " (with recursive kataloggrupper as ( "
				+ " select grpid, 0 as prevgrpid, rubrik, text, sortorder, html, 0 as depth , array[ sortorder, grpid] as sortpath, grpid as avdelning from artgrp where prevgrpid = " + StartupData.getKatalogRootGrp() + " and grpid not in (" + s + ")" 
				+ " union all select a.grpid, a.prevgrpid, a.rubrik, a.text, a.sortorder, a.html, depth+1 , sortpath || a.sortorder || a.grpid, avdelning from artgrp a  "
				+ " join kataloggrupper  on kataloggrupper.grpid=a.prevgrpid and kataloggrupper.grpid not in (" + s + ")) "
				+ " select * from kataloggrupper order by sortpath) ";*/
	}
	
	public static String getSQLKatalogGrupper(Integer rootGrp, boolean includeRootGrp) {
		return getSQLKatalogGrupper(rootGrp, includeRootGrp, false);
	}
	
	public static String getSQLKatalogGrupper() {
		return getSQLKatalogGrupper(StartupData.getKatalogRootGrp(), false);
	}	

	public static String getSQLKatalogGrupperKort() {
		return getSQLKatalogGrupper(StartupData.getKatalogRootGrp(), false, true);
	}	
	public static String getSQLKatalogGrupperKort(Integer rootGrp) {
		return getSQLKatalogGrupper(rootGrp, false, true);
	}	
	public static String getSQLKatalogGrupperKort(Integer rootGrp,boolean includeRootGrp) {
		return getSQLKatalogGrupper(rootGrp, includeRootGrp, true);
	}	
	

	// returnerar grupper i en grupp. returnerar ine info om antal klasar mm. Grupperna returneras även om de är med i inställningarnas exclude grupllista
	public static List<KatalogGrupp> getGrupperInGrupp(Connection con, Integer grpid)  throws SQLException {
		if (grpid==null) return null;
		List<KatalogGrupp> akg = new ArrayList<>();
		
		String q = "select grpid, prevgrpid, sortorder, rubrik, text, infourl, html, htmlfoot, htmlhead, visaundergrupper from artgrp where prevgrpid= ? order by sortorder, grpid";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, grpid);
		ResultSet rs = ps.executeQuery();
		KatalogGrupp kg;
		while (rs.next()) {
			kg = new KatalogGrupp(rs.getInt("grpid"), rs.getInt("prevgrpid"), rs.getInt("sortorder"), rs.getString("rubrik"), rs.getString("text"), rs.getString("html"), null, null, null, null, rs.getString("htmlhead"), rs.getString("htmlfoot"), rs.getBoolean("visaundergrupper"));
			akg.add(kg); 
		}
		return akg;
		
	}
	
	public static boolean saveGrupp(Connection con, int grpid, int sortorder, String rubrik, String text, String htmlHead, String htmlFoot, boolean visaundergrupper) throws SQLException {
		String q = "update artgrp set  sortorder=?, rubrik=?, text=?, htmlhead=?, htmlfoot=?, visaundergrupper=? where grpid= ?";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, sortorder);
		ps.setString(2, rubrik );
		ps.setString(3, text);
		ps.setString(4, htmlHead );
		ps.setString(5, htmlFoot);
		ps.setBoolean(6, visaundergrupper);
		ps.setInt(7, grpid);
		int r = ps.executeUpdate();
		return r > 0;		
	}
	
	public static KatalogGrupp getGrupp(Connection con, Integer grpid)  throws SQLException {
		if (grpid==null) return null;
		
		String q = "select * from artgrp kgl where grpid= ? order by sortorder, grpid";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, grpid);
		ResultSet rs = ps.executeQuery();
		KatalogGrupp kg=null;
		if (rs.next()) {
			kg = new KatalogGrupp(rs.getInt("grpid"), rs.getInt("prevgrpid"), rs.getInt("sortorder"), rs.getString("rubrik"), rs.getString("text"), rs.getString("html"), null, null, null, null, rs.getString("htmlhead"), rs.getString("htmlfoot"), rs.getBoolean("visaundergrupper"));
		}
		return kg;
		
	}
	
	public static KatalogGruppLista getKatalogGruppLista(Connection con) throws SQLException {
		KatalogGruppLista kgl = new KatalogGruppLista();
		ArrayList<KatalogGrupp> avdelningar = null;
		if (StartupData.isFirstTradLevelAvdelning()) {
			avdelningar = new ArrayList<>();
		}
		kgl.setAvdelningar(avdelningar);
		String q = "select kgl.grpid, kgl.prevgrpid, kgl.rubrik, kgl.text, kgl.sortorder, kgl.html ,kgl.depth, kgl.sortpath, count(distinct agl.klasid), kgl.avdelning, kgl.path, kgl.htmlfoot, kgl.htmlhead, kgl.visaundergrupper from " + getSQLKatalogGrupper() + " kgl "
				+ " left outer join artgrplank agl on agl.grpid=kgl.grpid "
				+ " group by kgl.grpid, kgl.prevgrpid, kgl.rubrik, kgl.text, kgl.sortorder, kgl.html ,kgl.depth, kgl.sortpath, kgl.avdelning, kgl.path, kgl.htmlfoot, kgl.htmlhead, kgl.visaundergrupper order by kgl.sortpath";
		PreparedStatement ps = con.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		KatalogGrupp kg;
		Integer avdelning = null;
		while (rs.next()) {
			if (rs.getInt(7) == 0) {
				avdelning = rs.getInt(1);
			}
			kg = new KatalogGrupp(rs.getInt(1), rs.getInt(2), rs.getInt(5), rs.getString(3), rs.getString(4), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getArray(11), rs.getInt(10), rs.getString("htmlhead"), rs.getString("htmlfoot"), rs.getBoolean("visaundergrupper"));
			kgl.addGrupp(kg);
			if (avdelningar != null && kg.getDepth() == 0) {
				avdelningar.add(kg);
			}
		}

		return kgl;
	}

/*	public static Produkt getProdukt2(Connection con, Integer klasId) throws SQLException {
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
*/
/*	public static ArrayList<Produkt> getProdukterCloseToID(Connection con, Integer klasId) throws SQLException {
		if (klasId == null) {
			klasId = 1;
		}
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "select * from (SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid > ? order by klasid limit 2) a  union "
				+ " select * from (SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid < ? order by klasid desc limit 2) b  order by klasid";
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
*/
/*	public static ArrayList<Produkt> getProdukterInGrupp3(Connection con, Integer grpId) throws SQLException {
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
*/
	
//	public static String ARTIKEL_SELECT_LIST = " a.nummer, a.namn, a.katnamn, a.utpris, a.enhet, a.minsaljpack, a.forpack, a.fraktvillkor ";
	/*
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
		pv.setBruttoPris(rs.getDouble(4+offset));
		pv.setKatNamn(rs.getString(3+offset));
		pv.setNamn(rs.getString(2+offset));
		pv.setArtnr(rs.getString(1+offset));
		pv.setAntalSaljPackIForpack(antalSaljPackIForpack);
		if (Const.isEmpty(pv.getKatNamn())) pv.setKatNamn(pv.getNamn());
		
		return pv;
	}*/

/*	public static Artikel gammalgetArtikel(Connection con, String artNr) throws SQLException {
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
*/	
	public static VarukorgRow getArtikelProdukt(Connection con, String artNr, Integer klasid, String kundnr, Integer lagernr) throws SQLException {
		String q = "select " + V_SELECT_COLS 
				+ " from vbutikart v  "
				+ " where (v.ak_klasid=? or 0=?) and v.nummer=? and v.k_nummer=? and v.lid_lagernr=? limit 1";
		PreparedStatement ps = con.prepareStatement(q);
                if (klasid==null) {
                    ps.setInt(1, 0);
                    ps.setInt(2, 0);
                
                } else {
                    ps.setInt(1, klasid);
                    ps.setInt(2, 1);
                }
		ps.setString(3, artNr);
		ps.setString(4, kundnr);
		ps.setInt(5, lagernr);
		ResultSet rs = ps.executeQuery();
		Artikel art=null;
		VarukorgRow vk = null;

		if (rs.next()) {
			vk = new VarukorgRow();
			art = getArtikelFromSQL2(rs);
			Produkt p = new Produkt();
			p.setKlasid(rs.getInt(1));
			p.setRubrik(rs.getString(2));
			p.setBeskrivning(rs.getString(3));
			p.setBeskrivningHTML(rs.getString(4));
			p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
			p.setAutoBildArtnr(SXUtil.isEmpty(rs.getString("bildartnr")) ? (SXUtil.isEmpty(rs.getString("auto_bildartnr")) ? art.getArtnr() : rs.getString("auto_bildartnr")) : rs.getString("bildartnr"));
                        p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
			vk.setArt(art);
			vk.setP(p);
		}
		
		return vk;
	}
	
	
/*	public static List<Artikel> gammalgetProduktVarianter(Connection con, Integer klasId) throws SQLException {
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
*/
/*
	public static ArrayList<Produkt> getLiknandeProdukterInGrp(Connection con, Integer klasId) throws SQLException {
		if (klasId == null) {
			klasId = 1;
		}
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "SELECT klasid, rubrik, text, infourl, fraktvillkor, html FROM ARTKLASE where klasid in "
				+ " (select  agl.klasid from artgrplank agl where agl.grpid in ( select grpid from artgrplank where klasid=? )) and klasid != ? "
				+ " order by klasid LIMIT 4";
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
*/
	public static SokResult sok(Connection con, String sokStr, String kundnr, Integer lagernr) throws SQLException {
		return sok(con, sokStr, kundnr, lagernr , 0, Const.getStartupData().getDefaultSokLimit());
	}
	public static SokResult sok(Connection con, String sokStr, String kundnr, Integer lagernr, int offset, int limit) throws SQLException {
		if (sokStr==null || sokStr.trim().length() <= 0) return null;
		
		String[] termer;
		termer = sokStr.split(" ");
		return termer.length>0 ? sok(con,termer, kundnr, lagernr , offset, limit) : null;
	}
	
	public static SokResult sok(Connection con, String[] sokTermer, String kundnr, Integer lagernr) throws SQLException{
		return sok(con, sokTermer, kundnr, lagernr ,0,Const.getStartupData().getDefaultSokLimit());
	}
	public static SokResult sok(Connection con, String[] sokTermer, String kundnr, Integer lagernr, int offset, int limit) throws SQLException{
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
		 select grpid, 0 as prevgrpid, r-ubrik, text, sortorder, html, 0 as depth , array[sortorder, grpid] as sortpath, grpid as avdelning from artgrp where prevgrpid = 0
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
"create temporary table tt (ak_klasid integer, sortvikt integer) on commit drop; "  +
"insert into tt select ak_klasid, sortvikt"
+ " from ( "
+ "select ak_klasid, sortvikt " +
" from " +
" ( " +
"" +
" select ak.klasid as ak_klasid, ak.rubrik as ak_rubrik, ak.text as ak_text, ak.html as ak_html, " +
" ak.auto_bildartnr as ak_auto_bildartnr, " +
" (sum(case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end) * 1 + " +
"sum(case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end) * 2 + " +
"sum(case when upper(ak.rubrik) like upper(terms.term)||'%' then 1 else 0 end) * 4 +  +" +
"sum(case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1 else 0 end)  * 1 + " +
"sum(case when ak.auto_sokartnr like '%-'||upper(terms.term)||'-%' then 1 else 0 end) * 400 +" +
"sum(case when ak.auto_sokartnr like '%-'||upper(terms.term)||'%' then 1 else 0 end) * 200 +" +
"sum(case when ak.auto_sokrefnr  like '%-'||upper(terms.term)||'-%' then 1 else 0 end) * 100) * autosortvikt as sortvikt " +
" from artklase ak " +
" join ( values" + valueString.toString() + " ) as terms (term) on 1=1 " +
" join artgrplank agl on ak.klasid=agl.klasid " +
" join " +
" " + getSQLKatalogGrupperKort() + " ag" +  " on ag.grpid = agl.grpid " +

" where (ak.auto_sokord) like  ('%'||terms.term||'%') " +
" group by ak.klasid, ak.rubrik, ak.text, ak.html, ak.auto_bildartnr, autosortvikt " +
" having count(distinct terms.term)=? " +
" ) aa " +
" order by sortvikt desc" +
" offset ? limit ?"
+ " ) aar; "
                        
+ "select "+ V_SELECT_COLS + ", sortvikt "
+ " from tt join vbutikart v on v.k_nummer=? and v.lid_lagernr=? and v.ak_klasid=tt.ak_klasid "	
+ " where v.ak_klasid = any ((select array_agg(tt.ak_klasid) from tt)::integer[]) " 
+" order by sortvikt desc, v.ak_klasid, v.akl_sortorder, v.nummer"				;
		/*-
		String q = 
				" select v.ak_klasid, v.ak_rubrik, v.ak_text,v.ak_html, "
				+ -" v.nummer, v.namn, v.katnamn, v.utpris, v.enhet, v.minsaljpack, v.forpack, v.fraktvillkor, "
				+ " v.lid_lagernr, v.l_ilager, v.l_maxlager, v.l_best, v.l_iorder, v.lid_bnamn, "
				+ " v.kundnetto_bas, v.kundnetto_staf1, v.kundnetto_staf2, v.staf_antal1, v.staf_antal2 "
				+" from vbutikart v "
				+ " join "
				+ " (select * from "
				
				+ "	(select klasid as klasid, "
				+ "		("
				+ "		sum(cnt_ag_rubrik)/ case when count(*) = 0 then 1 else count(*) end * 1 + "
				+ "		sum(cnt_ak_rubrik)  / case when count(*) = 0 then 1 else count(*) end * 2 + "
				+ "		sum(cnt_ak_rubrik_start) / case when count(*) = 0 then 1 else count(*) end * 4 + "
				+ "		sum(cnt_ak_text) / case when count(*) = 0 then 1 else count(*) end *1 + "
				+ "		sum(cnt_artnr) * 6 + "
				+ "		sum(cnt_refnr) * 4) * autosortvikt as vikt, "
				+ "		count(*) as antal from ("
				+ "		SELECT ak.klasid as klasid, terms.term as term, ak.autosortvikt as autosortvikt,"

				+ "		case when upper(ag.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ag_rubrik ,"
				+ "		case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ak_rubrik, "
				+ "		case when upper(ak.rubrik) like upper(terms.term)||'%' then 1 else 0 end as cnt_ak_rubrik_start, "
				+ "		case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ak_text, "
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 1 else 0 end as cnt_artnr, "
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 1 else 0 end as cnt_refnr "

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
				+ "	group by klasid, autosortvikt"
				+ "	having count(distinct term) >= ? "
				+ ") klasar2 "
				+ " order by  "
				+ "vikt desc "
				+ "	offset ? limit ? ) klasar"
				
				+ " on klasar.klasid = v.ak_klasid "
				+ " where v.k_nummer=? and v.lid_lagernr=? "
//				+ " order by klasar.antal desc , klasar.vikt*v.ak._autosortvikt desc, v.ak_rubrik, v.akl_sortorder, v.nummer"
				+ " order by  klasar.vikt desc"
				+ ", v.ak_rubrik, v.akl_sortorder, v.nummer"
				
				;
*/		
/*		String q = 
				" select v.ak_klasid, v.ak_rubrik, v.ak_text,v.ak_html, "
				+ " v.nummer, v.namn, v.katnamn, v.utpris, v.enhet, v.minsaljpack, v.forpack, v.fraktvillkor, "
				+ " v.lid_lagernr, v.l_ilager, v.l_maxlager, v.l_best, v.l_iorder, v.lid_bnamn, "
				+ " v.kundnetto_bas, v.kundnetto_staf1, v.kundnetto_staf2, v.staf_antal1, v.staf_antal2 "
				+" from vbutikart v "
				+ " join "
				+ " (select * from "
				
				+ "	(select klasid as klasid, autosortvikt as autosortvikt, "
				+ "		sum(cnt_ag_rubrik)/ case when count(*) = 0 then 1 else count(*) end as cnt_ag_rubrik, "
				+ "		sum(cnt_ak_rubrik)  / case when count(*) = 0 then 1 else count(*) end as cnt_ak_rubrik, "
				+ "		sum(cnt_ak_rubrik_start) / case when count(*) = 0 then 1 else count(*) end as cnt_ak_rubrik_start, "
				+ "		sum(cnt_ak_text) / case when count(*) = 0 then 1 else count(*) end as cnt_ak_text, "
				+ "		sum(cnt_artnr) as cnt_artnr, "
				+ "		sum(cnt_refnr) as cnt_refnr, "
				+ "		count(*) as antal from ("
				+ "		SELECT ak.klasid as klasid, terms.term as term, ak.autosortvikt as autosortvikt,"

				+ "		case when upper(ag.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ag_rubrik ,"
				+ "		case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ak_rubrik, "
				+ "		case when upper(ak.rubrik) like upper(terms.term)||'%' then 1 else 0 end as cnt_ak_rubrik_start, "
				+ "		case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1 else 0 end as cnt_ak_text, "
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 1 else 0 end as cnt_artnr, "
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 1 else 0 end as cnt_refnr "

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
				+ "	group by klasid, autosortvikt"
				+ "	having count(distinct term) >= ? "
				+ ") klasar2 "
				+ " order by  ("
				+ "		klasar2.cnt_ag_rubrik*1 +"
				+ "		klasar2.cnt_ak_rubrik*2 +"
				+ "		klasar2.cnt_ak_rubrik_start*4 +"
				+ "		klasar2.cnt_ak_text*1 +"
				+ "		klasar2.cnt_artnr*6 +"
				+ "		klasar2.cnt_refnr*4 "
				+ ")*klasar2.autosortvikt desc"
				+ "	offset ? limit ? ) klasar"
				
				+ " on klasar.klasid = v.ak_klasid "
				+ " where v.k_nummer=? and v.lid_lagernr=? "
//				+ " order by klasar.antal desc , klasar.vikt*v.ak._autosortvikt desc, v.ak_rubrik, v.akl_sortorder, v.nummer"
				+ " order by  ("
				+ "		klasar.cnt_ag_rubrik*1 +"
				+ "		klasar.cnt_ak_rubrik*2 +"
				+ "		klasar.cnt_ak_rubrik_start*4 +"
				+ "		klasar.cnt_ak_text*1 +"
				+ "		klasar.cnt_artnr*6 +"
				+ "		klasar.cnt_refnr*4 "
				+ ")*v.ak_autosortvikt desc, v.ak_rubrik, v.akl_sortorder, v.nummer"
				
				;
*/		

/*		String q = 
				" select v.ak_klasid, v.ak_rubrik, v.ak_text,v.ak_html, "
				+ " v.nummer, v.namn, v.katnamn, v.utpris, v.enhet, v.minsaljpack, v.forpack, v.fraktvillkor, "
				+ " v.lid_lagernr, v.l_ilager, v.l_maxlager, v.l_best, v.l_iorder, v.lid_bnamn, "
				+ " v.kundnetto_bas, v.kundnetto_staf1, v.kundnetto_staf2, v.staf_antal1, v.staf_antal2 "
				+" from vbutikart v "
				+ " join "
				+ "	(select klasid as klasid, sum(vikt) as vikt, count(*) as antal from ("
				+ "		SELECT ak.klasid as klasid, terms.term as term, ("

				+ "		case when upper(ag.rubrik) like '%'||upper(terms.term)||'%' then 1000 else 0 end +"
				+ "		case when upper(ak.rubrik) like '%'||upper(terms.term)||'%' then case when upper(ak.rubrik) like upper(terms.term)||'%' then 4000 else 2000 end else 0 end +"
				+ "		case when upper(ak.text) like '%'||upper(terms.term)||'%' then 1000 else 0 end  +"
				+ "		case when string_agg('-'||a.nummer::text||'-',' ') like '%-'||upper(terms.term)||'-%' then 6000 else 0 end  +"
				+ "		case when string_agg('-'||a.refnr::text||'-'||a.enummer||'-'||a.rsk,' ') like '%-'||upper(terms.term)||'-%' then 4000 else 0 end  "
				+ "		)  "
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
				
				+ " on klasar.klasid = v.ak_klasid "
				+ " where v.k_nummer=? and v.lid_lagernr=? "
//				+ " order by klasar.antal desc , klasar.vikt*v.ak._autosortvikt desc, v.ak_rubrik, v.akl_sortorder, v.nummer"
				+ " order by  klasar.vikt*v.ak_autosortvikt desc, v.ak_rubrik, v.akl_sortorder, v.nummer"
				
				;
*/		
		PreparedStatement ps = con.prepareStatement(q);
		int i = 0;
		for (String s: sokTermer) {
			i++;
			ps.setString(i, s.toUpperCase());
		}
		i++;
		ps.setInt(i, sokTermer.length);
		i++;
		ps.setInt(i, offset);
		i++;
		ps.setInt(i, limit);                
		i++;
		ps.setString(i, kundnr);
		i++;
		ps.setInt(i, lagernr);
		i++;
		
                ResultSet rs=null;
                //Hitta första reusltesetet i sql-satsen
                boolean hasMoreResultSets =ps.execute();
                while (hasMoreResultSets || ps.getUpdateCount()!=-1) {
                    if ( hasMoreResultSets ) {  
                        rs = ps.getResultSet();
                        break;
                    } else { 
                        int queryResult = ps.getUpdateCount();  
                        if ( queryResult == -1 ) { // no more queries processed  
                            break;  
                        } 
                    } 
                    hasMoreResultSets = ps.getMoreResults();                    
                }
                
		//ResultSet rs = ps.executeQuery();
		SokResult sokResultat = new SokResult();
		SokResultRow row;
		Produkt p=null;
		Artikel pv=null;
		
		while (rs.next()) {
			if (p==null || !p.getKlasid().equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				sokResultat.add(p);
				p.setKlasid(rs.getInt(1));
				p.setRubrik(rs.getString(2));
				p.setBeskrivningHTML(rs.getString(4));
				p.setBeskrivning(rs.getString(3));
				p.setAutoBildArtnr(SXUtil.isEmpty(rs.getString("bildartnr")) ? (SXUtil.isEmpty(rs.getString("auto_bildartnr")) ? rs.getString("nummer") : rs.getString("auto_bildartnr")) : rs.getString("bildartnr"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
                                p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
			}
			
			pv = getArtikelFromSQL2(rs);
			p.getVarianter().add(pv);
			
			LagerSaldo ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			pv.addLagerSaldoRow(ls);
			
		}
		return sokResultat;
	}
	
	
	public static User adminLoginAsUser(Connection con, String anvandarnamn) throws SQLException{
            return login(con, anvandarnamn, "", true);
        }
        
	public static User login(Connection con, String anvandarnamn, String losen) throws SQLException{
            return login(con, anvandarnamn, losen, false);
        }
	public static User login(Connection con, String anvandarnamn, String losen, boolean adminLoginAsUser) throws SQLException{
		User u = null;
		if (anvandarnamn!=null && losen!=null) {
			String q = 
					"select kk.namn, kk.epost, kk.kontaktid, k.nummer, k.namn, k.saljare, kl.loginnamn, kl.defaultlagernr, kl.defaultinkmoms, kl.defaultfraktsatt " +
					" from kund k join kundkontakt kk on kk.kundnr=k.nummer join kundlogin kl on kl.kontaktid=kk.kontaktid " +
					" where kl.loginnamn=? and (kl.loginlosen=? or 0=?)";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, anvandarnamn);
			ps.setString(2, losen);
                        ps.setInt(3, adminLoginAsUser ? 0 : 1);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.setKontaktId(rs.getInt(3));
				u.setKontaktMail(rs.getString(2));
				u.setKontaktNamn(rs.getString(1));
				u.setKundNamn(rs.getString(5));
				u.setKundnr(rs.getString(4));
				u.setKundSaljare(rs.getString(6));
				u.setLoguinNamn(rs.getString(7));
				u.setDefaultLagernr(rs.getInt(8));
				u.setDefaultInkMoms(rs.getBoolean(9));
				u.setDefaultFraktsatt(rs.getString(10));
			} else {
                            Const.log("Hittar inte användare " + anvandarnamn + " och angivet lösenord i kundlogin");
                        }
		}
		return u;
	}

		
	public static String loginSkapaNyttLosen(Connection con, String anvandarnamn) throws SQLException{
		String nyttLosen = null;

		if (anvandarnamn!=null) {
			String q = 
					"select kl.loginnamn " +
					" from kund k join kundkontakt kk on kk.kundnr=k.nummer join kundlogin kl on kl.kontaktid=kk.kontaktid " +
					" where kl.loginnamn=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, anvandarnamn);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				nyttLosen = Const.getRandomString(8);
				q = "update kundlogin set loginlosen=? where loginnamn=?";
				ps = con.prepareStatement(q);
				ps.setString(1, nyttLosen);
				ps.setString(2, anvandarnamn);
				int res = ps.executeUpdate();
				if (res == 0) return null;
			}
		}
		return nyttLosen;
	}

	public static String getUserEpostFromSQL(Connection con, String anvandarnamn) throws SQLException{

		String epost=null;
		if (anvandarnamn!=null) {
			String q = 
					"select kk.epost " +
					" from kund k join kundkontakt kk on kk.kundnr=k.nummer join kundlogin kl on kl.kontaktid=kk.kontaktid " +
					" where kl.loginnamn=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, anvandarnamn);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				epost = rs.getString(1);
				if (SXUtil.isEmpty(epost)) epost=null;
			}
		}
		return epost;
	}
	
	
	public static void logoutAutoLogin(Connection con, User u) throws SQLException {
		if (u!=null && u.getAutoLoginUuid()!=null) {
			String q = "delete from butikautologin where expiredate < current_date or uuid=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, u.getAutoLoginUuid());
			ps.executeUpdate();
		}
	}
	
	
	public static User autoLogin(Connection con, String autoLoginId) throws SQLException{
		User u = null;
		if (autoLoginId!=null) {
			String q = "select kk.namn, kk.epost, kk.kontaktid, k.nummer, k.namn, k.saljare, kl.loginnamn, kl.defaultlagernr, kl.defaultinkmoms "
					+ " from kund k, kundkontakt kk, kundlogin kl, butikautologin bl "
					+ " where k.nummer = kk.kundnr and kk.kontaktid = kl.kontaktid and bl.kontaktid=kl.kontaktid "
					+ " and bl.uuid=? and bl.expiredate >= current_date";
			
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, autoLoginId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.setKontaktId(rs.getInt(3));
				u.setKontaktMail(rs.getString(2));
				u.setKontaktNamn(rs.getString(1));
				u.setKundNamn(rs.getString(5));
				u.setKundnr(rs.getString(4));
				u.setKundSaljare(rs.getString(6));
				u.setLoguinNamn(rs.getString(7));
				u.setDefaultLagernr(rs.getInt(8));
				u.setAutoLoginUuid(autoLoginId);
				u.setDefaultInkMoms(rs.getBoolean(9));
			}
		}
		return u;
	}
	
	
//	public static ArrayList<Produkt> getProdukterInGrupp(Connection con, Integer grpId) throws SQLException {
//		return getProdukterInGrupp(con, grpId, Const.getDefaultKundnr());
//	}
	public static ArrayList<Produkt> getProdukterInGrupp(Connection con, Integer grpId, String kundnr) throws SQLException {
		return getProdukterInGrupp(con, grpId, kundnr, false);
	}

	public static ArrayList<Produkt> getProdukterInGrupp(Connection con, Integer grpId, String kundnr, boolean allaLagersaldon) throws SQLException {
		ArrayList<Produkt> produkter = new ArrayList<>();
		String q = "select  " + V_SELECT_COLS
				+ " from vbutikart v join artgrplank agl on agl.klasid=v.ak_klasid "
				+ " where agl.grpid=? and v.k_nummer=? and v.l_lagernr ";
		
		if (allaLagersaldon) q += " in ( " + StartupData.getAktivaLagernrAsString() + " ) ";
		else q += " = " + StartupData.getDefultLagernr() + " ";
		
		q += " order by agl.sortorder, v.ak_klasid, v.akl_sortorder, v.nummer, v.lid_lagernr";

		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, grpId);
		ps.setString(2, kundnr);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		Integer temp_klasid=null;
		String temp_artnr=null;
		Artikel a=null;
		LagerSaldo ls=null;
		while (rs.next()) {
			if (temp_klasid== null || !temp_klasid.equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				p.setKlasid(rs.getInt("ak_klasid"));
				p.setRubrik(rs.getString("ak_rubrik"));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString("ak_html")) ? SXUtil.toHtml(rs.getString("ak_text")) : rs.getString("ak_html"));
				p.setBeskrivning(rs.getString("ak_text"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
				temp_klasid = p.getKlasid();
				p.setAutoBildArtnr(SXUtil.isEmpty(rs.getString("bildartnr")) ? (SXUtil.isEmpty(rs.getString("auto_bildartnr")) ? rs.getString("nummer") : rs.getString("auto_bildartnr")) : rs.getString("bildartnr"));
	                        p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
                		produkter.add(p);

			}
			if (temp_artnr==null || !temp_artnr.equals(rs.getString("nummer"))) {
				a = getArtikelFromSQL2(rs);
				p.addVariant(a);
				temp_artnr = rs.getString("nummer");
			}
			
			ls  = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			a.addLagerSaldoRow(ls);
		}

		return produkter;
	}

	
	public static LagerSaldo getLagerSaldo(Connection con, String artnr, int lagernr) throws SQLException {
		String q = "select lid.bnamn as lid_bnamn, l.lagernr as lid_lagernr, l.ilager as l_ilager, l.maxlager as l_maxlager, l.best as l_best, l.iorder as l_iorder from lager l join lagerid lid on lid.lagernr=l.lagernr where l.artnr=? and l.lagernr=?";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(1, artnr);
		ps.setInt(2, lagernr);
		ResultSet rs = ps.executeQuery();
		LagerSaldo ls=new LagerSaldo();
		if (rs.next()) {
			ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
                        ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
		} else {
			ls.setBest(0.0);
			ls.setIlager(0.0);
			ls.setIorder(0.0);
			ls.setLagernamn("");
			ls.setLagernr(lagernr);
			ls.setMaxlager(0.0);
		}
		return ls;
	}
	
	
	
	public static Artikel getArtikelFromSQL2(ResultSet rs) throws SQLException {
		Double forpack;
		Double minSaljPack;
		Double antalSaljPack;
		Integer antalSaljPackIForpack;
		Artikel pv = new Artikel();
		forpack = rs.getDouble("forpack");
		minSaljPack = rs.getDouble("minsaljpack");
		if (forpack.compareTo(0.0) == 0) forpack=1.0;
		//antalSaljPack = forpack;
		antalSaljPack=1.0;
		if (minSaljPack.compareTo(0.0) > 0) {
			antalSaljPack = minSaljPack;
		}
		if (antalSaljPack.compareTo(0.0) == 0) antalSaljPack = 1.0;
		if (forpack % antalSaljPack == 0 && forpack.compareTo(antalSaljPack) > 0) { //Om vi har jämt delbart förhållande mellan förpack och minsajpack
			antalSaljPackIForpack = ((Double)(forpack / antalSaljPack)).intValue();
		} else {
			antalSaljPackIForpack = 1;
		}
		
		pv.setForpack(forpack);
		pv.setAntalSaljpack(antalSaljPack);
		pv.setEnhet(rs.getString("enhet"));
		pv.setLevVillkor(rs.getInt("fraktvillkor"));
                pv.setBruttoPris(rs.getDouble("utpris"));
		pv.setBruttopris_staf1(rs.getDouble("staf_pris1"));
		pv.setBruttopris_staf2(rs.getDouble("staf_pris2"));
		pv.setNettoPrisExMoms(rs.getDouble("kundnetto_bas"));
		pv.setNettoPrisStaf1ExMoms(rs.getDouble("kundnetto_staf1"));
		pv.setNettoPrisStaf2ExMoms(rs.getDouble("kundnetto_staf2"));
		pv.setAntalStaf1(rs.getDouble("staf_antal1"));
		pv.setAntalStaf2(rs.getDouble("staf_antal2"));
		pv.setKatNamn(rs.getString("katnamn"));
		pv.setNamn(rs.getString("namn"));
		pv.setArtnr(rs.getString("nummer"));
		pv.setRsk(rs.getString("rsk"));
		pv.setAntalSaljPackIForpack(antalSaljPackIForpack);
		if (Const.isEmpty(pv.getKatNamn())) pv.setKatNamn(pv.getNamn());
		
		return pv;
	}
	
	
//	public static Produkt getProdukt(Connection con, Integer klasId) throws SQLException {
//		return getProdukt(con, klasId, Const.getDefaultKundnr());
//	}
	public static Produkt getProdukt(Connection con, Integer klasId, String kundnr) throws SQLException {
		String q = "select  " + V_SELECT_COLS
				+ " from vbutikart v  "
				+ " where v.k_nummer=? and v.ak_klasid=?"
				+ " order by v.akl_sortorder, v.nummer, v.lid_lagernr ";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(2, klasId);
		ps.setString(1, kundnr);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		Integer temp_klasid=null;
		String temp_artnr=null;
		Artikel a=null;
		while (rs.next()) {
			if (temp_klasid== null || !temp_klasid.equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				p.setKlasid(rs.getInt("ak_klasid"));
				p.setRubrik(rs.getString("ak_rubrik"));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString("ak_html")) ? SXUtil.toHtml(rs.getString("ak_text")) : rs.getString("ak_html"));
				p.setBeskrivning(rs.getString("ak_text"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
				p.setAutoBildArtnr(SXUtil.isEmpty(rs.getString("bildartnr")) ? (SXUtil.isEmpty(rs.getString("auto_bildartnr")) ? rs.getString("nummer") : rs.getString("auto_bildartnr")) : rs.getString("bildartnr"));
                                p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
				temp_klasid = p.getKlasid();
			}
			
			if (temp_artnr==null || !temp_artnr.equals(rs.getString("nummer"))) {
				a = getArtikelFromSQL2(rs);
				p.addVariant(a);
				temp_artnr = rs.getString("nummer");
			}
			LagerSaldo ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			a.addLagerSaldoRow(ls);
		}

		return p;
		
	}


	
	
	public static Produkt getProduktFromArtnr(Connection con, String artnr, String kundnr, int lagernr) throws SQLException {
		String q = "select  " + V_SELECT_COLS
				+ " from vbutikart v  "
				+ " where v.k_nummer=? and v.lid_lagernr=? and v.ak_klasid= "
				+ " (select min(akl.klasid)  "
				+ " from " + getSQLKatalogGrupper() + " ag"

				+ "		join artgrplank agl on ag.grpid=agl.grpid "
				+ "		join artklaselank akl on akl.klasid=agl.klasid "
				+ "		where akl.artnr=? ) "
				+ " order by v.akl_sortorder, v.nummer, v.lid_lagernr ";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(3, artnr);
		ps.setInt(2, lagernr);
		ps.setString(1, kundnr);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		Integer temp_klasid=null;
		String temp_artnr=null;
		Artikel a=null;
		while (rs.next()) {
			if (temp_klasid== null || !temp_klasid.equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				p.setKlasid(rs.getInt("ak_klasid"));
				p.setRubrik(rs.getString("ak_rubrik"));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString("ak_html")) ? SXUtil.toHtml(rs.getString("ak_text")) : rs.getString("ak_html"));
				p.setBeskrivning(rs.getString("ak_text"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
				p.setAutoBildArtnr(SXUtil.isEmpty(rs.getString("bildartnr")) ? (SXUtil.isEmpty(rs.getString("auto_bildartnr")) ? rs.getString("nummer") : rs.getString("auto_bildartnr")) : rs.getString("bildartnr"));
                                p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
				temp_klasid = p.getKlasid();
			}
			
			if (temp_artnr==null || !temp_artnr.equals(rs.getString("nummer"))) {
				a = getArtikelFromSQL2(rs);
				p.addVariant(a);
				temp_artnr = rs.getString("nummer");
			}
			
			LagerSaldo ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			a.addLagerSaldoRow(ls);
		}

		return p;
		
	}

        public static ArrayList<KatalogGrupp> getGrupperForProduktEndastRubrikOchID(Connection con, Integer klasid) throws SQLException{
            if (klasid==null) return null;
            String q = "select ag.grpid, ag.rubrik from artgrplank agl join artgrp ag on ag.grpid=agl.grpid where agl.klasid=" + klasid 
                    + " and ag.grpid in ( select grpid from ( " + getSQLKatalogGrupper() + " ) agg ) order by ag.grpid";
            ResultSet rs = con.prepareStatement(q).executeQuery();
            KatalogGrupp k;
            ArrayList<KatalogGrupp> al = new ArrayList<>();
            while (rs.next()) {
                k = new KatalogGrupp();
                k.setRubrik(rs.getString(2));
                k.setGrpId(rs.getInt(1));
                al.add(k);
            }
            return al;
        }
	
	        
	public static ArrayList<Produkt> getToplistaInGrupp(HttpServletRequest request, Integer gruppId, String kundnr, int lagernr, int maxResults) throws SQLException {
		if (!Const.getSessionData(request).getKatalogGruppLista(Const.getConnection(request)).isGruppInList(gruppId))  return null;
		return getToplistaInGrupp(Const.getConnection(request), gruppId, kundnr, lagernr, maxResults);
	}
	
	public static ArrayList<Produkt> getToplistaInGrupp(Connection con, Integer gruppId, String kundnr, int lagernr, int maxResults) throws SQLException {
		ArrayList<Produkt> ret = new ArrayList<>();
		
				String q = "select  " + V_SELECT_COLS
				+ " from vbutikart v  "
				+ " where v.k_nummer=? and v.lid_lagernr=? and v.ak_klasid in "
				+ " (select ak.klasid  "
				+ " from " + getSQLKatalogGrupperKort(gruppId, true) + " ag"

				+ "		join artgrplank agl on ag.grpid=agl.grpid "
				+ "     join artklase ak on ak.klasid=agl.klasid"
				+ "		order by ak.autosortvikt desc limit ? "
				+ "		 ) "
				+ " order by v.ak_klasid, v.akl_sortorder, v.nummer, v.lid_lagernr ";
//System.out.print(q);

		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(3, maxResults);
		ps.setInt(2, lagernr);
		ps.setString(1, kundnr);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		Integer temp_klasid=null;
		String temp_artnr=null;
		Artikel a=null;
		while (rs.next()) {
//System.out.print("loop");
			if (temp_klasid== null || !temp_klasid.equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				ret.add(p);
				p.setKlasid(rs.getInt("ak_klasid"));
				p.setRubrik(rs.getString("ak_rubrik"));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString("ak_html")) ? SXUtil.toHtml(rs.getString("ak_text")) : rs.getString("ak_html"));
				p.setBeskrivning(rs.getString("ak_text"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
                                p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
				temp_klasid = p.getKlasid();
			}
			
			if (temp_artnr==null || !temp_artnr.equals(rs.getString("nummer"))) {
				a = getArtikelFromSQL2(rs);
				p.addVariant(a);
				temp_artnr = rs.getString("nummer");
			}
			
			LagerSaldo ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			a.addLagerSaldoRow(ls);
		}

		return ret;
		
	}
	
	
	public static ArrayList<Produkt> getRekommenderadeToplistaInGrupp(Connection con, Integer gruppId, String kundnr, int lagernr, int maxResults) throws SQLException {
		ArrayList<Produkt> ret = new ArrayList<>();
		
				String q = "select  " + V_SELECT_COLS
				+ " from vbutikart v  "
				+ " where v.k_nummer=? and v.lid_lagernr=? and v.ak_klasid in "
				+ " (select ak.klasid  "
				+ " from " + getSQLKatalogGrupper(gruppId, true) + " ag"

				+ "		join artgrplank agl on ag.grpid=agl.grpid "
				+ "     join artklase ak on ak.klasid=agl.klasid and ak.rekommenderadprio > 0 "
				+ "		order by ak.rekommenderadprio desc, ak.autosortvikt desc, ak.klasid limit ? "
				+ "		 ) "
				+ " order by v.ak_klasid, v.akl_sortorder, v.nummer, v.lid_lagernr ";
//System.out.print(q);

		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(3, maxResults);
		ps.setInt(2, lagernr);
		ps.setString(1, kundnr);
		ResultSet rs = ps.executeQuery();
		Produkt p = null;
		Integer temp_klasid=null;
		String temp_artnr=null;
		Artikel a=null;
		while (rs.next()) {
//System.out.print("loop");
			if (temp_klasid== null || !temp_klasid.equals(rs.getInt("ak_klasid"))) {
				p = new Produkt();
				ret.add(p);
				p.setKlasid(rs.getInt("ak_klasid"));
				p.setRubrik(rs.getString("ak_rubrik"));
				p.setBeskrivningHTML(SXUtil.isEmpty(rs.getString("ak_html")) ? SXUtil.toHtml(rs.getString("ak_text")) : rs.getString("ak_html"));
				p.setBeskrivning(rs.getString("ak_text"));
				p.setAutoSamkoptaKlasar(rs.getString("ak_auto_samkopta_klasar"));
                                p.setWebBeskrivningFranGrpid(rs.getInt("ak_webbeskrivningfrangrpid"));
				temp_klasid = p.getKlasid();
			}
			
			if (temp_artnr==null || !temp_artnr.equals(rs.getString("nummer"))) {
				a = getArtikelFromSQL2(rs);
				p.addVariant(a);
				temp_artnr = rs.getString("nummer");
			}
			
			LagerSaldo ls = new LagerSaldo();
			ls.setBest(rs.getDouble("l_best"));
			ls.setIlager(rs.getDouble("l_ilager"));
			ls.setIorder(rs.getDouble("l_iorder"));
			ls.setMaxlager(rs.getDouble("l_maxlager"));
			ls.setLagernamn(rs.getString("lid_bnamn"));
			ls.setLagernr(rs.getInt("lid_lagernr"));
			a.addLagerSaldoRow(ls);
		}

		return ret;
		
	}

	
	public static ArrayList<Integer> getAllaProduktIds(Connection con) throws SQLException {
		String q = "select distinct agl.klasid " //+ V_SELECT_COLS
				+ " from artgrplank agl  "
				+ " join  "
				+  getSQLKatalogGrupper() + " ag"
				+ " on ag.grpid=agl.grpid ";
		PreparedStatement ps = con.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		ArrayList<Integer> ret = new ArrayList<>();
		while (rs.next()) {
			ret.add(rs.getInt(1));
		}

		return ret;
		
	}

	public static void setKundloginLagernr(Connection con, String loginNamn, Integer lagernr) throws SQLException{
		PreparedStatement ps = con.prepareStatement("update kundlogin set defaultlagernr=? where loginnamn=?");
		ps.setInt(1, lagernr);
		ps.setString(2, loginNamn);
		if (ps.executeUpdate()<1) throw new SQLException("Loginnamnet hittades inte");		
	}
	public static void setKundloginInkmoms(Connection con, String loginNamn, Boolean inkmoms) throws SQLException{
		PreparedStatement ps = con.prepareStatement("update kundlogin set defaultinkmoms=? where loginnamn=?");
		ps.setBoolean(1, inkmoms);
		ps.setString(2, loginNamn);
		if (ps.executeUpdate()<1) throw new SQLException("Loginnamnet hittades inte");		
	}
	public static void setKundloginFraktsatt(Connection con, String loginNamn, String fraktsatt) throws SQLException{
		PreparedStatement ps = con.prepareStatement("update kundlogin set defaultfraktsatt=? where loginnamn=?");
		ps.setString(1, fraktsatt);
		ps.setString(2, loginNamn);
		if (ps.executeUpdate()<1) throw new SQLException("Loginnamnet hittades inte");		
	}
	
	
	public static Kund getKund(Connection con, String kundnr) throws SQLException {
		String q = " select k.nummer, k.namn, k.adr1, k.adr2, k.adr3, k.lnamn, k.ladr2, k.ladr3, k.saljare, k.tel, k.biltel, k.regnr, k.kgrans, k.ktid, " +
" k.nettolst, k.bonus, k.mottagarfrakt, k.fraktbolag, k.fraktfrigrans, k.distrikt, k.kravordermarke, k.linjenr1, k.linjenr2, k.linjenr3, k.skickafakturaepost " +
" , coalesce(sum(kr.tot),0) as reskontra, coalesce(sum(case when kr.falldat < current_date-30 then kr.tot else 0 end), 0) as reskontr30 " +
" , coalesce(sum(case when kr.falldat >= current_date-30 and kr.tot < 0 then kr.tot else 0 end), 0) as reskontrkreditEjforfallen30 " +				
" from kund k left outer join kundres kr on kr.kundnr=k.nummer " +
" where kundnr=? group by k.nummer";
		Kund k = null;
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(1, kundnr);
		ResultSet rs= ps.executeQuery();
		if (rs.next()) {
			k = new Kund();
			k.setKundnr(rs.getString(1));
			k.setNamn(rs.getString(2));
			k.setAdr1(rs.getString(3));
			k.setAdr2(rs.getString(4));
			k.setAdr3(rs.getString(5));
			k.setLnamn(rs.getString(6));
			k.setLadr2(rs.getString(7));
			k.setLadr3(rs.getString(8));
			k.setSaljare(rs.getString(9));
			k.setTel(rs.getString(10));
			k.setBiltel(rs.getString(11));
			k.setRegnr(rs.getString(12));
			k.setKgrans(rs.getDouble(13));
			k.setKtid(rs.getInt(14));
			k.setNettolst(rs.getString(15));
			k.setBonus(rs.getInt(16));
			k.setMottagarfrakt(rs.getInt(17)!=0);
			k.setFraktbolag(rs.getString(18));
			k.setFraktfrigrans(rs.getDouble(19));
			k.setDistrikt(rs.getInt(20));
			k.setKravordermarke(rs.getInt(21)!=0);
			k.setLinjenr1(rs.getString(22));
			k.setLinjenr2(rs.getString(23));
			k.setLinjenr3(rs.getString(24));
			k.setSkickafakturaepost(rs.getInt(25)!=0);
			k.setReskontra(rs.getDouble(26));
			k.setReskontraForfall30(rs.getDouble(27));
			k.setReskontraKreditEjForfallen30(rs.getDouble(28));
		}
		return k;
	}
	public static void log(Connection con, Integer kontaktId, String servlet, String id) {
		try {
			PreparedStatement ps = con.prepareStatement("insert into hemsidalog (kontaktid, servlet, id) values (?,?,?)");
			ps.setInt(1, kontaktId);
			ps.setString(2, servlet);
			ps.setString(3, id);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	public static VeckodagarSet getTurbilsdagar(Connection con, String kundnr, int lagernr) throws SQLException {
		//Returnerar vilka veckodagar som det går en tirbil från vald filial till kunden
		String q = "select sum(t.d1) as d1, sum(t.d2) as d2, sum(t.d3) as d3, sum(t.d4) as d4, sum(t.d5) as d5 from turlinje t join kund k on t.linjenr=k.linjenr1 or t.linjenr=k.linjenr2 or t.linjenr=k.linjenr3 " +
		" where k.nummer = ? and t.franfilial=? " +
		" group by t.franfilial order by t.franfilial";
		PreparedStatement ps = con.prepareStatement(q);
		ps.setString(1, kundnr);
		ps.setInt(2,lagernr);
		ResultSet rs= ps.executeQuery();
		VeckodagarSet dagar = null;
		if (rs.next()) {
			dagar = new VeckodagarSet();
			dagar.setMandag(rs.getInt(1)>0);
			dagar.setTisdag(rs.getInt(2)>0);
			dagar.setOnsdag(rs.getInt(3)>0);
			dagar.setTorsdag(rs.getInt(4)>0);
			dagar.setFredag(rs.getInt(5)>0);
		}
		return dagar;
	}
}
