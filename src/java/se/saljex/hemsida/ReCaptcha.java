/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ulf
 */
public class ReCaptcha {

	public static String RECAPTCHA_HTMLELEMENT = "g-recaptcha";
	public static String RECAPTCHA_HTMLFORMNAME = "g-recaptcha-response";
	
	public static boolean check(HttpServletRequest request) {
		return check(Const.getReCaptchaSecretKey(Const.getConnection(request)), request.getParameter(RECAPTCHA_HTMLFORMNAME));
	}
	
	public static boolean check(String secret, String response) {
		if (secret==null || response==null) return false;
		boolean ret = false;
		try {
			URL url = new URL("https://www.google.com/recaptcha/api/siteverify?secret=" + Const.urlEncode(secret) + "&response=" + Const.urlEncode(response) );
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			int cn=0;
			while ((line = reader.readLine()) != null) {
			  cn++;
			  if (cn==2) { 
				  if (line.endsWith("true")) {
					  ret=true;
					  break;
				  } 
			  }
			}
			reader.close();
		} catch (java.net.MalformedURLException e) { e.printStackTrace(); }
		catch (IOException ie) { ie.printStackTrace(); }
		
		return ret;
	}
/*	
	public ReCaptchaResponse checkAnswer(String remoteAddr, String challenge, String response) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/");
		String postParameters = "privatekey=" + URLEncoder.encode(privateKey) + "&remoteip=" + URLEncoder.encode(remoteAddr) +
			"&challenge=" + URLEncoder.encode(challenge) + "&response=" + URLEncoder.encode(response);

		String message = httpLoader.httpPost(VERIFY_URL, postParameters);

		if (message == null) {
			return new ReCaptchaResponse(false, "Null read from server.");
		}

		String[] a = message.split("\r?\n");
		if (a.length < 1) {
			return new ReCaptchaResponse(false, "No answer returned from recaptcha: " + message);
		}
		boolean valid = "true".equals(a[0]);
		String errorMessage = null;
		if (!valid) {
			if (a.length > 1)
				errorMessage = a[1];
			else
				errorMessage = "recaptcha4j-missing-error-message";
		}
		
		return new ReCaptchaResponse(valid, errorMessage);
	}

*/
}
