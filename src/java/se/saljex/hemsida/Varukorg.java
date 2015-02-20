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

	public Varukorg(Connection con, Integer kontaktID) throws SQLException{
		if (con==null || kontaktID==null) return;
		else {
			rows = getSQLVarukorg(con, kontaktID);
		}
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
	
	public void addArtikel(Connection con, HttpServletRequest request, Integer kontaktId, Integer kid, String artnr, Integer antal) throws SQLException{
		if (antal==null || kid==null || artnr==null) return;

		if (kontaktId!=null) {
				updateOrAddSQLVarukorg(con, false, kontaktId, kid, artnr, antal);
				rows = getSQLVarukorg(con, kontaktId);
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
			setArtikel(con, Const.getSessionData(request).getAvtalsKundnr(), Const.getSessionData(request).getLagerNr(), kontaktId, kid, artnr, nyttAntal);
		}
	}	
	
	public void setArtikel(Connection con, String kundnr, Integer lagernr, Integer kontaktId, Integer kid, String artnr, Integer antal) throws SQLException{
			if (antal==null || kid==null || artnr==null) return;
			
			if (kontaktId!=null) {
					if (antal.equals(0)) {
						removeSQLVarukorg(con, kontaktId, kid, artnr);
					} else {
						updateOrAddSQLVarukorg(con, true, kontaktId, kid, artnr, antal);				
					}
					rows = getSQLVarukorg(con, kontaktId);
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
						removeSQLVarukorg(con, kontaktId, kid, artnr);
						vkProdukt.getVarukorgArtiklar().remove(vkArtikel);
						if (vkProdukt.getVarukorgArtiklar().size() <= 0) rows.remove(vkProdukt);
					}
				}
				
			}
		
	}	
	
	public void loadFromSQLAndSetVarukorg(Connection con, Integer kontaktID) throws SQLException{
		if (con==null || kontaktID==null) return;
		rows = getSQLVarukorg(con, kontaktID);
	}
	
	private List<VarukorgProdukt> getSQLVarukorg(Connection con, Integer kontaktID) throws SQLException{
		if (con==null || kontaktID==null) return null;
		List<VarukorgProdukt> l = new ArrayList<>();
		PreparedStatement ps = con.prepareStatement("select ak.rubrik, ak.text, ak.infourl, ak.fraktvillkor, ak.html, v.klasid, v.artnr, v.antal, " + SQLHandler.ARTIKEL_SELECT_LIST + " from vkorg_view v join artikel a on a.nummer=v.artnr join artklase ak on ak.klasid=v.klasid  where kontaktid=? order by v.klase_ch_timestamp, v.akl_sortorder, v.artnr");
		ps.setInt(1, kontaktID);
		ResultSet rs = ps.executeQuery();
		VarukorgProdukt vp = null;
		Produkt p;
		Artikel a;
		while(rs.next()) {
			if (vp==null || !vp.getProdukt().getKlasid().equals(rs.getInt(6))) {
				vp = new VarukorgProdukt();
				p = new Produkt();
				p.setRubrik(rs.getString(1));
				p.setBeskrivning(rs.getString(2));
				p.setBeskrivningHTML(rs.getString(5));
				p.setKlasid(rs.getInt(6));
				vp.setProdukt(p);
				l.add(vp);
			}
			a = SQLHandler.getArtikelFromSQL(rs, 8);
			vp.addArtikel(a, rs.getInt(8));
		}
		return l;
	}
	
	// setantal = antalet är absolut, setantal=false = antalet är relativt och skall adderas till nuvarande
	public void updateOrAddSQLVarukorg(Connection con, boolean setAntal, Integer kontaktid, Integer klasid, String artnr, Integer antal) throws SQLException{
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
	
	public void removeSQLVarukorg(Connection con, Integer kontaktid, Integer klasid, String artnr) throws SQLException{
		if (kontaktid==null || klasid==null || artnr==null) return;
		PreparedStatement ps = con.prepareStatement("delete from vkorg where kontaktid=? and typ='VK' and klasid=? and artnr=?");
		ps.setInt(1, kontaktid);
		ps.setInt(2, klasid);
		ps.setString(3, artnr);
		ps.execute();
	}
	
	//Läs in SQL-varukorgen och sätt ihop den med aktuell varukorg
	public void mergeSQLVarukorg(Connection con, HttpServletRequest request, Integer kontakid) throws SQLException{
		List<VarukorgProdukt> ny = rows;
				
		rows = getSQLVarukorg(con, kontakid);
		for (VarukorgProdukt p : ny) {
			for (VarukorgArtikel a : p.getVarukorgArtiklar()) {
				addArtikel(con, request, kontakid, p.getProdukt().getKlasid(), a.getArtnr(), a.getAntal());
			}
		}
	}
	
}
