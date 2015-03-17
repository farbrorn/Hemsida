/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * Web application lifecycle listener.
 *
 * @author Ulf
 */
public class MainListener implements ServletContextListener {
	@javax.annotation.Resource(mappedName = "sxadm")
	private DataSource sxadm;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Const.log("---------Startar Hemisda");
		Const.log("MAinListener - Läser in sxreg");
		StartupData startupData = new StartupData(sxadm);
		int dadaLast=0;
		try {
			Const.setStartupData(startupData);
			int dataLast = startupData.loadConfig();
			Const.log("Antal inlästa poster " + dataLast);
		} catch (SQLException e) {
			Const.log("Fel vid inläsning av data: " + e.toString());
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.print("--------- Stänger Hemsida");
	}
}
