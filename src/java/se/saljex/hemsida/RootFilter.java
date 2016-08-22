/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 *
 * @author Ulf
 */
public class RootFilter implements Filter {
    
	@javax.annotation.Resource(mappedName = "sxadm")
	private DataSource sxadm;
    
    public RootFilter() {
    }    
    

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        InitData initDat = new InitData(request);
		Const.setInitdata(request, initDat);
	
		String data=null;
		Cookie[] cookies = ((HttpServletRequest)request).getCookies();
		if (cookies!=null) for(Cookie cookie : cookies){
			if(DataCookieHandler.COOKIENAME.equals(cookie.getName())) {
				data = cookie.getValue();
				break;
			}
		}
		initDat.setDataCookie(new DataCookieHandler(data));
		
		//Stäng av cache
		((HttpServletResponse)response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //http 1.1
		((HttpServletResponse)response).setHeader("Pragma", "no-cache"); //http 1.0
		((HttpServletResponse)response).setHeader("Expires", "0"); // Proxies.
	
	
        try {
	    initDat.setCon(sxadm.getConnection());
		Const.getSessionData((HttpServletRequest)request).autoLogin(Const.getConnection(request), (HttpServletRequest)request);
	    chain.doFilter(request, response);
	} catch (SQLException e) { e.printStackTrace(); throw  new IOException("SQL-fel");
        } finally {
            try { initDat.getCon().close(); } catch (Exception e) {}
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    
}
