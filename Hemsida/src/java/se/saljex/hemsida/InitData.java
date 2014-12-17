/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.Connection;

/**
 *
 * @author Ulf
 */
public class InitData {

	private long uniktID=0;
	
    public InitData() {
    }
    
    java.sql.Connection con = null;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
	public long getNewUniktID() {
		return uniktID++;
	}


}
