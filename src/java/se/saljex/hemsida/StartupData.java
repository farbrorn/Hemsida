/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import com.sun.xml.xsom.impl.scd.Iterators;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import static se.saljex.hemsida.Const.getStartupData;

/**
 *
 * @author Ulf
 */
public class StartupData {
	HashMap<String, String> configMap = new HashMap<>();
	HashMap<Integer, LagerEnhet> lagerEnhetMap = new HashMap<>();
	ArrayList<String> cardsLeftTop= new ArrayList<>();
	ArrayList<String> cardsLeftBot=new ArrayList<>();
	ArrayList<String> cardsMidTop=new ArrayList<>();
	ArrayList<String> cardsMidBot=new ArrayList<>();
	ArrayList<String> cardsRightTop=new ArrayList<>();
	ArrayList<String> cardsRightBot=new ArrayList<>();
	
	DataSource sxadm;
	
	private Double momsMultiplikatot=null;
			
			
	
	public StartupData(	DataSource sxadm) {
		this.sxadm=sxadm;
	}

	
	public static String getImageServerCacheAbsolutPath() {
		return Const.getStartupData().getConfig("Hemsida-Imageserver-CachPath", "/dum/imageserver/cache");
	}

	public static Integer getKatalogRootGrp() {
		Integer ret = null;
		try {
			ret = new Integer(Const.getStartupData().getConfig("Hemsida-KatalogRootGrp", "0"));
		} catch (NumberFormatException e) {
		}
		return ret != null ? ret : 0;
	}

	public static String getSxServSmtpPassword() {
		return Const.getStartupData().getConfig("SxServSMTPPassword", null);
	}

	public static boolean isHemsidaTestlage() {
		return !"false".equals(Const.getStartupData().getConfig("Hemsida-Testlage", "true"));
	}

	public static boolean isFirstTradLevelAvdelning() {
		return "true".equals(Const.getStartupData().getConfig("Hemsida-IsFirstTradLevelAvdelning", "true"));
	}

	public static String getReCaptchaSecretKey() {
		return Const.getStartupData().getConfig("Hemsida-ReCaptcha-SecretKey", null);
	}

	public static String getLogoUrl() {
		return Const.getStartupData().getConfig("Hemsida-LogoUrl", "http://www.saljex.se/p/s200/logo-saljex.png");
	}

	public static String getPageMeny() {
		return Const.getStartupData().getConfig("Hemsida-PageMeny", "hem");
	}

	public static String getPageHome() {
		return Const.getStartupData().getConfig("Hemsida-PageMeny", "hem");
	}

	public static String getReCaptchaSiteKey() {
		return Const.getStartupData().getConfig("Hemsida-ReCaptcha-SiteKey", null);
	}

	public static String getDefaultKundnr() {
		return Const.getStartupData().getConfig("Hemsida-Default-Kundnr", "1");
	}

	public static String getSxServSmtpUser() {
		return Const.getStartupData().getConfig("SxServSMTPUser", null);
	}

	public static boolean redirectToHttps() {
		return "true".equals(Const.getStartupData().getConfig("Hemsida-Redirect-To-HTTPS", "false"));
	}

	public static String getSxServAdminMail() {
		return Const.getStartupData().getConfig("SxServAdminMail", "ulf@saljex.se");
	}

	public static List getKatalogExcludeGrp() {
		ArrayList<Integer> ret = new ArrayList<>();
		Integer delInt;
		String s = Const.getStartupData().getConfig("Hemsida-KatalogExcludeGrps", "999998,999999");
		if (s != null) {
			String[] sp = s.split(",");
			for (String d : sp) {
				try {
					delInt = new Integer(d);
					if (delInt != null) {
						ret.add(delInt);
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return ret;
	}

	public static String getKatalogExcludeGrpAsString() {
		String s = "";
		List<Integer> l = getKatalogExcludeGrp();
		for (Integer i : l) {
			if (!s.isEmpty()) {
				s = s + ",";
			}
			s = s + i;
		}
		return s;
	}

	public static String getSxServOrderMail() {
		return Const.getStartupData().getConfig("SxServOrderMail", "order@saljex.se");
	}

	public static String getSxServSmtpTransport() {
		return Const.getStartupData().getConfig("SxServSMTPTransport", "smtp");
	}

	public static String getSxServMailFromAddress() {
		return Const.getStartupData().getConfig("SxServMailFromAddress", "info@saljex.se");
	}

	public static String getImageServerOriginalAbsolutPath() {
		return Const.getStartupData().getConfig("Hemsida-Imageserver-OriginalPath", "/dum/imageserver");
	}

	public static String getForetagNamn() {
		return "S\u00e4ljex";
	}

	public static String getSxServSmtpServerPort() {
		return Const.getStartupData().getConfig("SxServSMTPServerPort", "25");
	}

	public static Integer getDefultLagernr() {
		Integer ret = null;
		try {
			ret = new Integer(Const.getStartupData().getConfig("Hemsida-Default-Lagernr", "0"));
		} catch (NumberFormatException e) {
		}
		return ret != null ? ret : 0;
	}

	public static String getAktivaLagernrAsString() {
		String s = "";
		List<Integer> l = getAktivaLagernr();
		for (Integer i : l) {
			if (!s.isEmpty()) {
				s = s + ",";
			}
			s = s + i;
		}
		return s;
	}

	public static List getAktivaLagernr() {
		ArrayList<Integer> ret = new ArrayList<>();
		Integer delInt;
		String s = Const.getStartupData().getConfig("Hemsida-AktivaLagernr", "0,1,3,4,10");
		if (s != null) {
			String[] sp = s.split(",");
			for (String d : sp) {
				try {
					delInt = new Integer(d);
					if (delInt != null) {
						ret.add(delInt);
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return ret;
	}

	public static Integer getKatalogTradMaxDisplayDepthLevel() {
		Integer ret = null;
		try {
			ret = new Integer(Const.getStartupData().getConfig("Hemsida-TradMaxDisplayDepth", "1"));
		} catch (NumberFormatException e) {
		}
		return ret != null ? ret : 1;
	}

	public  int getDefaultSokLimit() {
		Integer ret = null;
		try {
			ret = new Integer(Const.getStartupData().getConfig("Hemsida-Default-Soklimit", "20"));
		} catch (NumberFormatException e) {
		}
		return ret != null ? ret : 20;
	}
	
	
	public Double getMomsMultiplikator() { 
		if (momsMultiplikatot==null) {
			try {
				momsMultiplikatot = new Double(getStartupData().getConfig("Hemsida-Momsmultiplikator", "1.25"));
			} catch (NumberFormatException e) { momsMultiplikatot=1.25 ; }
		}
		return momsMultiplikatot;
	}
	
	public int loadConfig() throws SQLException{
		Connection con=null;
		HashMap<String, String> ny;
		try {
			con = sxadm.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select id, varde from sxreg where id like 'Hemsida-%'");
			ny = new HashMap<>();
			while (rs.next()) {
				ny.put(rs.getString(1), rs.getString(2));
				System.out.print(rs.getString(1) + " - " + rs.getString(2));
			}
			configMap = ny;
			
			rs = con.createStatement().executeQuery("select lagernr, bnamn, levadr1, levadr2, levadr3, tel, fax, email from lagerid where lagernr in (" + Const.getStartupData().getAktivaLagernrAsString() + ")" );
			LagerEnhet le;
			while (rs.next()) {
				le = new LagerEnhet();
				le.setLagernr(rs.getInt(1));
				le.setNamn(rs.getString(2));
				le.setAdress1(rs.getString(3));
				le.setAdress2(rs.getString(5));
				le.setTel(rs.getString(6));
				le.setEpost(rs.getString(8));
				addLagerEnhet(le);
			}
			
			rs = con.createStatement().executeQuery("select sidid, status, rubrik, html from hemsidasidor where status like 'card-%' order by sidid" );
			String t;
			String html;
			while (rs.next()) {
				t = rs.getString(2);
				html = rs.getString(4);
				if (t!=null && html !=null) { 
					if (t.startsWith(Page.STATUS_CARD_LEFT_TOP)) cardsLeftTop.add(html);
					else if (t.startsWith(Page.STATUS_CARD_LEFT_BOT)) cardsLeftBot.add(html);
					else if (t.startsWith(Page.STATUS_CARD_MID_TOP)) cardsMidTop.add(html);
					else if (t.startsWith(Page.STATUS_CARD_MID_BOT)) cardsMidBot.add(html);
					else if (t.startsWith(Page.STATUS_CARD_RIGHT_TOP)) cardsRightTop.add(html);
					else if (t.startsWith(Page.STATUS_CARD_RIGHT_BOT)) cardsRightBot.add(html);
				}
			}
					
		} finally {
			try { con.close(); }catch (Exception e) {}
		}
		return ny.size();
	}
	
	public String getConfig(String id, String defaultValue) {
		String ret;
		ret = configMap.get(id);
		if (ret==null) {
			Connection con=null;
			try {
				con=sxadm.getConnection();
				PreparedStatement ps = con.prepareStatement("select varde from sxreg where id=?");
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					configMap.put(id, rs.getString(1));
					return rs.getString(1);
				} else {
					ps = con.prepareStatement("insert into sxreg (id,varde) values (?,?)");
					ps.setString(1, id);
					ps.setString(2, defaultValue);
					ps.executeUpdate();
					return defaultValue;
				}
			} catch(SQLException se) { 
				Const.log("Fel vid behandling av sxreg - ID: " + id);
				se.printStackTrace();
			} finally {
				try {con.close(); } catch (Exception e) {}
			}
		}
		return ret;
	}

	public void addLagerEnhet(LagerEnhet le) {
		lagerEnhetMap.put(le.getLagernr(), le);
	}
	public LagerEnhet getLagerEnhet(Integer lagernr) {
		return lagerEnhetMap.get(lagernr);
	}
	public Map<Integer, LagerEnhet> getLagerEnhetList() {
		return lagerEnhetMap;
	}

	public ArrayList<String> getCardsLeftTop() {
		return cardsLeftTop;
	}

	public ArrayList<String> getCardsLeftBot() {
		return cardsLeftBot;
	}

	public ArrayList<String> getCardsMidTop() {
		return cardsMidTop;
	}

	public ArrayList<String> getCardsMidBot() {
		return cardsMidBot;
	}

	public ArrayList<String> getCardsRightTop() {
		return cardsRightTop;
	}

	public ArrayList<String> getCardsRightBot() {
		return cardsRightBot;
	}

	public DataSource getSxadm() {
		return sxadm;
	}
	
}
