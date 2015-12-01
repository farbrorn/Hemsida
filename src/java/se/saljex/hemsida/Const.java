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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static final String ATTRIB_TILLBEHOR_PRODUKTER = "tillbehor-produkter";
    public static final String ATTRIB_SAMKOPTA_PRODUKTER = "samkopta-produkter";
    public static final String ATTRIB_KATALOGGRUPPLISTA = "kataloggrupplista";
    public static final String ATTRIB_KATALOGHEADERINFO = "katalogheaderinfo";
    public static final String ATTRIB_KATALOGAVDELNING = "katalogavdelning";
    public static final String ATTRIB_KATALOGREQUESTEDGRUPP = "katalogrequestedgrupp";
    public static final String ATTRIB_SESSIONDATA = "sessiondata";
    public static final String ATTRIB_AID = "artikelnummer";
    public static final String ATTRIB_KID = "klasid";
	
	
	public static final String PARAM_VARUKORG_AC = "vkaction";
	public static final String PARAM_VARUKORG_AC_ADD = "add";
	public static final String PARAM_VARUKORG_AC_SET = "set";
	public static final String PARAM_VARUKORG_AC_REMOVE = "remove";
	public static final String PARAM_VARUKORG_AC_SAVEORDER = "save";
	public static final String PARAM_VARUKORG_GET = "get";
	public static final String PARAM_VARUKORG_GET_AJAX = "ax";
	public static final String PARAM_ARTNR = "aid";
	public static final String PARAM_KLASID = "kid";
	public static final String PARAM_ANTAL = "qty";

	public static final String PARAM_LOGINNAMN = "loginnamn";
	public static final String PARAM_LOGINLOSENL = "loginlosen";
	
		public final static String COOKIEAUTOINLOGID="loginuuid";
		private static final Logger logger = Logger.getLogger("Hemsida");	

		
	private static StartupData startupData=null;
	
	
	
	public static void log(String s) { logger.log(Level.WARNING, s); }
	
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
	 
	public static String toHtmlNotLineBreak(String string) {
		return toHtml(string, false);
	}
	public static String toHtml(String string) {
		return toHtml(string, true);
	}
	public static String toHtml(String string, boolean convertWhites) {
		// Baserat på kod från http://www.rgagnon.com/javadetails/java-0306.html av S. Bayer
		if (string == null) return "";
		StringBuilder sb = new StringBuilder(string.length());
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
				if (convertWhites && lastWasBlankChar) {
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
				else if (convertWhites && c == '\n') sb.append("<br/>");
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
	
	public static String getSaljpackFormaeradEnhet(Artikel a) {
		return a.getAntalSaljpack().equals(1.0) ? getFormatEnhet(a.getEnhet()) : "x " + getAnpassade2Decimaler(a.getAntalSaljpack()) + getFormatEnhet(a.getEnhet());
	}
	
	public static String getArtBildURL(String artnr, Integer size) {
		if (size == null ) size=50;
		String bu = getStartupData().getBildURLLocal();
		if (!bu.endsWith("/")) bu=bu+"/";
		return bu + "s" + size + "/" + artnr + ".png";
	}
	public static String getArtBildURL(String artnr) {
		return getArtBildURL(artnr, 50);
	}
	
	public static String getArtBildURL(Produkt p, int size) {
		if (p.getAutoBildArtnr()!=null) return getArtBildURL(p.getAutoBildArtnr(),size);
		else if (p.getVarianter().size() > 0) return getArtBildURL(p.getVarianter().get(0).getArtnr(),size);
		else return "";
	}
	public static String getArtBildURL(Produkt p) {
		return getArtBildURL(p,50);
	}
	public static String getArtStorBildURL(Produkt p) {
		return getArtBildURL(p,200);
	}
	
	public static String getArtBildURL(ProduktGrund p) {
		return p.getAutoBildArtnr()!=null ? getArtBildURL(p.getAutoBildArtnr()) : "";
	}

	
	public static boolean isLagernrInAktivList(int lagernr) {
		List<Integer> list = StartupData.getAktivaLagernr();
		for (Integer l : list) {
			if (l.equals(lagernr)) return true;
		}
		return false;
	}
	

	public static StartupData getStartupData() {
		return startupData;
	}

	public static void setStartupData(StartupData startupData) {
		Const.startupData = startupData;
	}
	
	
	
}
