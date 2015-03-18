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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ulf
 */
public class Varukorg {

	public Varukorg(HttpServletRequest request) throws SQLException{
			rows = getSQLVarukorg(request);
	}

	public Varukorg() {
	}
	
	
	
	private List<VarukorgProdukt> rows = new ArrayList<>();

	public List<VarukorgProdukt> getVarukorgProdukter() {
		return rows;
	}

	public void setRows(ArrayList<VarukorgProdukt> rows) {
		this.rows = rows;
	}

	
	private VarukorgProdukt getRow(Integer kid) {
		VarukorgProdukt row = null;
		if (kid==null) return null;
		for (VarukorgProdukt p : rows) {
			if (p.getProdukt() != null) {
				if (kid.equals(p.getProdukt().getKlasid()) ) {
					row = p;
					break;
				}
			}
		}
		return row;
	}
	
	private void moveToTop(VarukorgProdukt vkp) {
		rows.remove(vkp);
		rows.add(vkp);
	}

	public void clearVarukorgRows(Connection con, Integer kontaktId) throws SQLException{
		if (kontaktId!=null) {
			PreparedStatement ps = con.prepareStatement("delete from vkorg where kontaktid=? and typ='VK'");
			ps.setInt(1, kontaktId);
			ps.executeUpdate();
		} 
		rows.clear();
	}
	

	public void setVarukorg(Connection con, HttpServletRequest request, List<VarukorgProdukt> newRows, Integer kontaktId ) throws SQLException{
		clearVarukorgRows(con, kontaktId);
		VarukorgProdukt vp;
		VarukorgArtikel va;
		for (int cn=newRows.size()-1; cn>=0; cn--) {
			vp = newRows.get(cn);
			for (int cn2=0 ; cn2<=vp.getVarukorgArtiklar().size()-1;  cn2++) {
				va = vp.getVarukorgArtiklar().get(cn2);
				addArtikel(request, vp.getProdukt().getKlasid(), va.getArtnr(), va.getAntal());
			}
		}
	}
	
	public void addArtikel(HttpServletRequest request, Integer kid, String artnr, Integer antal) throws SQLException{
		if (antal==null || kid==null || artnr==null) return;
		Connection con = Const.getConnection(request);
		Integer kontaktId  = Const.getSessionData(request).getInloggadKontaktId();
		if (kontaktId!=null) {
				updateOrAddSQLVarukorg(request, false,  kid, artnr, antal);
				rows = getSQLVarukorg(request);
		} else {
			VarukorgArtikel vkArtikel = null;
			VarukorgProdukt vkProdukt = getRow(kid);
			if (vkProdukt!=null) {
				vkArtikel = vkProdukt.getVariant(artnr);
				moveToTop(vkProdukt);
			}
			Integer nyttAntal=null;
			if (vkArtikel!=null) nyttAntal = vkArtikel.getAntal();
			if (nyttAntal==null) nyttAntal=0;
			nyttAntal+=antal;
			setArtikel(request, kid, artnr, nyttAntal);
		}
	}	
	
	public void setArtikel(HttpServletRequest request, Integer kid, String artnr, Integer antal) throws SQLException{
			if (antal==null || kid==null || artnr==null) return;
			Connection con = Const.getConnection(request);
			Integer kontaktId  = Const.getSessionData(request).getInloggadKontaktId();
			Integer lagernr = Const.getSessionData(request).getLagerNr();
			String kundnr = Const.getSessionData(request).getAvtalsKundnr();
			if (kontaktId!=null) {
					if (antal.equals(0)) {
						removeSQLVarukorg(request, kid, artnr);
					} else {
						updateOrAddSQLVarukorg(request, true, kid, artnr, antal);				
					}
					rows = getSQLVarukorg(request);
			} else {
				VarukorgArtikel vkArtikel = null;
				VarukorgProdukt vkProdukt = getRow(kid);
				if (vkProdukt!=null) {
					vkArtikel = vkProdukt.getVariant(artnr);
				}
				if (antal > 0) {
					if (vkArtikel==null) {
						VarukorgRow vr = SQLHandler.getArtikelProdukt(con, artnr, kid, kundnr, lagernr);
						if (vr!=null) {
							if (vkProdukt==null) { 
								vkProdukt = new VarukorgProdukt();
								vkProdukt.setProdukt(vr.getP());
								rows.add(vkProdukt);
							}
							vkProdukt.addArtikel(vr.getArt(), antal);
						}
					} else { 
						vkArtikel.setAntal(antal);
					}
				} else { //Ta bort artikeln
					if (vkArtikel!=null && vkProdukt!=null) {
						removeSQLVarukorg(request, kid, artnr);
						vkProdukt.getVarukorgArtiklar().remove(vkArtikel);
						if (vkProdukt.getVarukorgArtiklar().size() <= 0) rows.remove(vkProdukt);
					}
				}
				
			}
		
	}	
	
	public void loadFromSQLAndSetVarukorg(HttpServletRequest request) throws SQLException{
		Integer kontaktID = Const.getSessionData(request).getInloggadKontaktId();
		if (kontaktID==null) return;
		rows = getSQLVarukorg(request);
	}
	
	private List<VarukorgProdukt> getSQLVarukorg(HttpServletRequest request) throws SQLException{
		Connection con = Const.getConnection(request);
		Integer kontaktID  = Const.getSessionData(request).getInloggadKontaktId();
		Integer lagernr = Const.getSessionData(request).getLagerNr();
		String kundnr = Const.getSessionData(request).getAvtalsKundnr();
		if (con==null || kontaktID==null) return null;
		List<VarukorgProdukt> l = new ArrayList<>();
		PreparedStatement ps = con.prepareStatement("select v.klasid, v.artnr, v.antal  from vkorg_view v join artikel a on a.nummer=v.artnr join artklase ak on ak.klasid=v.klasid  where v.kontaktid=? order by v.klase_ch_timestamp, v.akl_sortorder, v.artnr");
		ps.setInt(1, kontaktID);
		ResultSet rs = ps.executeQuery();
		VarukorgProdukt vp = null;
		Produkt p;
		Artikel a;
		while(rs.next()) {
			VarukorgRow vr = SQLHandler.getArtikelProdukt(con, rs.getString(2), rs.getInt(1), kundnr, lagernr);
			if (vr!=null) {
				vp = getOrAddRow(l,vr.getP());
				vp.addArtikel(vr.getArt(), rs.getInt(3));
			}
		}
		return l;
	}
	
	private VarukorgProdukt getOrAddRow(List<VarukorgProdukt> rows, Produkt produkt) {
		for (VarukorgProdukt p : rows) {
			if (p.getProdukt().getKlasid().equals(produkt.getKlasid())) return p;
		}
		VarukorgProdukt vp = new VarukorgProdukt();
		vp.setProdukt(produkt);
		rows.add(vp);
		return vp;
	}
	
	// setantal = antalet är absolut, setantal=false = antalet är relativt och skall adderas till nuvarande
	public void updateOrAddSQLVarukorg(HttpServletRequest request, boolean setAntal, Integer klasid, String artnr, Integer antal) throws SQLException{
		Connection con = Const.getConnection(request);
		Integer kontaktid  = Const.getSessionData(request).getInloggadKontaktId();
//		Integer lagernr = Const.getSessionData(request).getLagerNr();
//		String kundnr = Const.getSessionData(request).getAvtalsKundnr();
		if (kontaktid==null || klasid==null || artnr==null || antal==null) return;
		String q;
		if (setAntal) {
			q = "update vkorg set antal=? , ch_timestamp=current_timestamp where kontaktid=? and typ='VK' and klasid=? and artnr=?";
		} else {
			q = "update vkorg set antal=antal+? , ch_timestamp=current_timestamp where kontaktid=? and typ='VK' and klasid=? and artnr=?";			
		}
		PreparedStatement ps = con.prepareStatement(q);
		ps.setInt(1, antal);
		ps.setInt(2, kontaktid);
		ps.setInt(3, klasid);
		ps.setString(4, artnr);
		if (ps.executeUpdate() < 1) {
			ps = con.prepareStatement("insert into vkorg (kontaktid, typ, klasid, artnr, antal ) values (?,'VK',?,?,?)");
			ps.setInt(1, kontaktid);
			ps.setInt(2, klasid);
			ps.setString(3, artnr);
			ps.setInt(4, antal);
			ps.executeUpdate();
		}
	}
	
	public void removeSQLVarukorg(HttpServletRequest request, Integer klasid, String artnr) throws SQLException{
		Connection con = Const.getConnection(request);
		Integer kontaktid  = Const.getSessionData(request).getInloggadKontaktId();
		if (kontaktid==null || klasid==null || artnr==null) return;
		PreparedStatement ps = con.prepareStatement("delete from vkorg where kontaktid=? and typ='VK' and klasid=? and artnr=?");
		ps.setInt(1, kontaktid);
		ps.setInt(2, klasid);
		ps.setString(3, artnr);
		ps.execute();
	}
	
	//Läs in SQL-varukorgen och sätt ihop den med aktuell varukorg
	public void mergeSQLVarukorg(HttpServletRequest request) throws SQLException{
		List<VarukorgProdukt> ny = rows;
				
		rows = getSQLVarukorg(request);
		for (VarukorgProdukt p : ny) {
			for (VarukorgArtikel a : p.getVarukorgArtiklar()) {
				addArtikel(request, p.getProdukt().getKlasid(), a.getArtnr(), a.getAntal());
			}
		}
	}
	
}
