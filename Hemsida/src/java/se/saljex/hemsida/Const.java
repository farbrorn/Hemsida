/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ulf
 */
public class Const {
    public static final String ATTRIB_NAME_INIT = "SXINITDATA";
    public static final String ATTRIB_PRODUKT = "produkt";
    public static final String ATTRIB_ROWCN = "radraknare";
    public static final String ATTRIB_LIKNANDE_PRODUKTER = "liknande-produkter";
    public static final String ATTRIB_KATALOGGRUPPLISTA = "kataloggrupplista";
    public static final String ATTRIB_KATALOGHEADERINFO = "katalogheaderinfo";
    public static final String ATTRIB_KATALOGAVDELNING = "katalogavdelning";
    public static final String ATTRIB_KATALOGREQUESTEDGRUPP = "katalogrequestedgrupp";
    public static final String ATTRIB_SESSIONDATA = "sessiondata";
	
	public static final String PARAM_VARUKORG_AC = "action";
	public static final String PARAM_VARUKORG_AC_ADD = "add";
	public static final String PARAM_VARUKORG_AC_SET = "set";
	public static final String PARAM_VARUKORG_AC_REMOVE = "remove";
	public static final String PARAM_VARUKORG_GET = "get";
	public static final String PARAM_VARUKORG_GET_AJAX = "ax";
	public static final String PARAM_ARTNR = "aid";
	public static final String PARAM_KLASID = "kid";
	public static final String PARAM_ANTAL = "qty";

	public static final String PARAM_LOGINNAMN = "loginnamn";
	public static final String PARAM_LOGINLOSENL = "loginlosen";
	
		public final static String COOKIEAUTOINLOGID="loginuuid";
	
	
	public static int getDefaultSokLimit() { return 20; }
	
	public static SessionData getSessionData(HttpServletRequest request) { 
		SessionData sd = (SessionData)request.getSession().getAttribute(ATTRIB_SESSIONDATA);
		if (sd==null) {
			sd = new SessionData();
			request.getSession().setAttribute(ATTRIB_SESSIONDATA, sd);
		}
		return sd;
	}
	
    public static InitData getInitData(ServletRequest request) {
		return (InitData)request.getAttribute(ATTRIB_NAME_INIT);
    }
    
	public static Integer getKatalogRootGrp() { 
	//	return 513; 
		return 0;
	}
	
	public static int[] getKatalogExcludeGrp() {
		int[] e = {895};
		return e;
	}
	public static String getKatalogExcludeGrpAsString() {
		String s="";
		for (int i : getKatalogExcludeGrp()) {
			if (!s.isEmpty()) s = s+",";
			s = s + i;
		}
		return s;
	}
	
	public static String getImageServerOriginalAbsolutPath() {
		return "/dum/imageserver";
	}
	public static String getImageServerCacheAbsolutPath() {
		return "/dum/imageserver/cache";
	}
	
	
	public static boolean isFirstTradLevelAvdelning() { return true; }
	
	public static Integer getKatalogTradMaxDisplayDepthLevel() { return 1; }
	
    public static void setInitdata(ServletRequest request, InitData initData) {
	request.setAttribute(ATTRIB_NAME_INIT, initData);
    }
	
	public static Connection getConnection(ServletRequest request) {
		return getInitData(request).getCon();
	}
	
	
	
    public static String getFormatDate(Date d) {
		 if (d != null) {
			 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 return simpleDateFormat.format(d);
		 } else { return ""; }
    }
	 
    public static String getFormatDate() {
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       return simpleDateFormat.format(new Date());
    }
    
    public static String getFormatNumber(Double tal, int decimaler) {
		  if (tal == null) return "";
        NumberFormat nf;
		  nf = NumberFormat.getInstance();
		  nf.setMaximumFractionDigits(decimaler);
		  nf.setMinimumFractionDigits(decimaler);
        return nf.format(tal);
    }

    public static String getFormatNumber(Float tal, int decimaler) {
		 return getFormatNumber(new Double(tal));
    }

    public static String getFormatNumber(Double tal) {
        return getFormatNumber(tal,2);
    }
    public static String getFormatNumber(Float tal) {
        return getFormatNumber(new Double(tal));
    }

	public static Double getRoundedDecimal(Double a) {	
		//Returnerar värdet avrundat till två decimaler
		return Math.round(a*100.0) / 100.0;
	}


    public static Date addDate(Date d, int dagar) {
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(d);
       calendar.add(Calendar.DATE, dagar);
       return calendar.getTime();
    }

	 public static boolean isEmpty(String s) {
		 if (s==null || s.trim().isEmpty()) return true; else return false;
	 }

	 public static Integer noNull(Integer a) {
		 if (a==null) return 0; else return a;
	 }
	 public static Double noNull(Double a) {
		 if (a==null) return 0.0; else return a;
	 }
	 
	 public static String toStr(String s) {
		 if (s == null) return ""; else return s;
	 }

	 //Tar bort avslutande tomma tecken
	 public static String rightTrim(String s) {
		 return s.replaceAll("\\s+$", "");
	 }

	 public static String urlEncode(String s) {
		 if (s == null) return ""; else try { return URLEncoder.encode(s, "UTF-8"); } catch (UnsupportedEncodingException e) {}
		 return "";//Om vi får exception retureneras ""
	 }
	 
	public static String toHtml(String string) {
		// Baserat på kod från http://www.rgagnon.com/javadetails/java-0306.html av S. Bayer
		if (string == null) return "";
		StringBuffer sb = new StringBuffer(string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = string.length();
		char c;

		for (int i = 0; i < len; i++)	{
			c = string.charAt(i);
			if (c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss 
				// word breaking
				if (lastWasBlankChar) {
					 lastWasBlankChar = false;
					 sb.append("&nbsp;");
				 } else {
					 lastWasBlankChar = true;
					 sb.append(' ');
				}
			} else {
            lastWasBlankChar = false;
				// HTML Special Chars
				if (c == '"') sb.append("&quot;");
				else if (c == '&') sb.append("&amp;");
				else if (c == '<') sb.append("&lt;");
				else if (c == '>') sb.append("&gt;");
				else if (c == '\n') sb.append("<br/>");
				else sb.append(c);
			}
		}
		return sb.toString();
	}

	public static final DecimalFormat DecimalFormatter2Dec = new DecimalFormat("###,###.00");
	public static final DecimalFormat DecimalFormatter0Dec = new DecimalFormat("###,###");
	public static String getAnpassade2Decimaler(Double tal) {
		if (tal==null) return "";
		DecimalFormat myFormatter;
		
		
		Long l = Math.round(tal*100);
		Long p = l/100;
		p = p*100; //Priset med 00 som ören
		if (p.equals(l)) { //priset har inga ören
			myFormatter = DecimalFormatter0Dec;
		} else {
			myFormatter = DecimalFormatter2Dec;
		}
		
		myFormatter.getDecimalFormatSymbols().setDecimalSeparator(',');
		myFormatter.getDecimalFormatSymbols().setGroupingSeparator(' ');
		return myFormatter.format(new Double(l)/100);
		
	}
	public static String getAnpassatPrisFormat(Double pris) {
		if (pris==null) return "";
		DecimalFormat myFormatter;
		
		
		Long l = Math.round(pris*100);
		Long p = l/100;
		p = p*100; //Priset med 00 som ören
		if (p.equals(l) && p.compareTo((long)10000)>0) { //priset har inga ören
			myFormatter = DecimalFormatter0Dec;
		} else {
			myFormatter = DecimalFormatter2Dec;
		}
		
		myFormatter.getDecimalFormatSymbols().setDecimalSeparator(',');
		myFormatter.getDecimalFormatSymbols().setGroupingSeparator(' ');
		return myFormatter.format(new Double(l)/100);
	}
	
	public static String getFormatEnhet(String enhet) {
		if (enhet==null) return "";
		String ret;
		switch (enhet.toUpperCase()) {
			case "ST" : ret = "st"; break;
			case "M2" : ret = "m²"; break; 
			case "M3" : ret = "m³"; break; 
			case "L" : ret = "l"; break; 
			case "SB" : ret = "sb"; break; 
			case "M" : ret = "m"; break; 
			case "KG" : ret = "Kg"; break; 
			case "PKT" : ret = "pkt"; break; 
			case "PAR" : ret = "par"; break; 
			default: ret=enhet; break;
		}
		return ret;
	}
	
	public static String getArtBildURL(String artnr, Integer size) {
		if (size == null ) size=50;
		return "http://saljex.se/p/s50/" + artnr + ".png";
	}
	public static String getArtBildURL(String artnr) {
		return getArtBildURL(artnr, 50);
	}
	
	public static String getArtBildURL(Produkt p) {
		return getArtBildURL(p.getVarianter().get(0).getArtnr());
	}
	
	public static String getDefaultKundnr() {
		return "1";
	}
	public static Integer getDefultLagernr() { return 0; }
	
}
