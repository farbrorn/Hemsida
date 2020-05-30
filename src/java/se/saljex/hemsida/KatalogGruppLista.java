/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Ulf
 */
public class KatalogGruppLista {
	private LinkedHashMap<Integer, KatalogGrupp> grupper= new LinkedHashMap<Integer, KatalogGrupp>();
	private ArrayList<KatalogGrupp> avdelningar=null;

	public boolean isGruppInList(Integer grpId) {
		return grupper.get(grpId)!=null;
	}
	
	public ArrayList<KatalogGrupp> getAvdelningar() {
		return avdelningar;
	}

	public void setAvdelningar(ArrayList<KatalogGrupp> avdelningar) {
		this.avdelningar = avdelningar;
	}
	
	public void addGrupp(KatalogGrupp grupp) {
		grupper.put(grupp.getGrpId(), grupp);
	}
	public LinkedHashMap<Integer, KatalogGrupp>  getGrupper() {return grupper; }
	
	public void getRootTreeToGrupp(Integer grp) {
		
	}

	public boolean isAvdelningar() { return avdelningar!=null; }
	
	public KatalogHeaderInfo getKatalogHeaderInfo(Integer grpId) {
		KatalogHeaderInfo khInfo = new KatalogHeaderInfo();
		Integer sd = null;
		ArrayList<KatalogGrupp> a = khInfo.getChildren();
		boolean childFardig=false;
		boolean nextPrevFardig=false;
		boolean harHittatGrupp=false;
		KatalogGrupp prevGrp = null;
		KatalogGrupp nextGrp = null;
		for (KatalogGrupp g : grupper.values()) {
			if (g.getGrpId().equals(grpId)) {
				khInfo.setKatalogGrupp(g);
				sd = g.getDepth()+1;
				harHittatGrupp=true;
			}
			if (!childFardig && sd!=null && !grpId.equals(g.getGrpId())) { 
				if (!sd.equals(g.getDepth())) childFardig=true; else a.add(g);
			}
			if (!nextPrevFardig) {
				if (!harHittatGrupp && !g.getGrpId().equals(grpId) && g.getAntalKlasar()>0) prevGrp=g;
				if (nextGrp==null && !g.getGrpId().equals(grpId) && harHittatGrupp && g.getAntalKlasar()>0 ) {
					nextGrp = g;
					nextPrevFardig=true;
				}
			}
			
			if (childFardig && nextPrevFardig) break;
		}
		khInfo.setNextGrp(nextGrp);
		khInfo.setPrevGrp(prevGrp);
		
		Integer[] sortPath = khInfo.getKatalogGrupp().getSortPath();
		if (sortPath!=null && sortPath.length > 1) {
			for (Integer p : sortPath) {
				if (!grpId.equals(p)) khInfo.getSokvag().add(grupper.get(p)); //Tainte med gruppen själv
			}
		}
		
		
		
		return khInfo;
	}
        
        public void loadCache(Connection con) throws SQLException{
            if (grupper==null) return;
            for (Map.Entry<Integer, KatalogGrupp> e : grupper.entrySet()) {
                e.getValue().getTopProdukter(con);
                e.getValue().getTopRekommenderadeProdukter(con);
            }
        }
	
}
