/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ulf
 -*/
public class HttpsFilter implements Filter {
	
	private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
	// this value is null, this filter instance is not currently
	// configured. 
	private FilterConfig filterConfig = null;
	
	public HttpsFilter() {
	}	

	@Override
	public void destroy() {
	}
	
	

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
		boolean noForward = false;
		noForward = "/robots.txt".equals(httpRequest.getRequestURI()) || "/sitemap.txt".equals(httpRequest.getRequestURI());
        if (!noForward && StartupData.redirectToHttps() && !httpRequest.isSecure() && !"https".equals(httpRequest.getHeader("X-Forwarded-Proto"))) {
            StringBuilder newUrl = new StringBuilder("https://");
            newUrl.append(httpRequest.getServerName());
            if (httpRequest.getRequestURI() != null) {
                newUrl.append(httpRequest.getRequestURI());
            }
            if (httpRequest.getQueryString() != null) {
                newUrl.append("?").append(httpRequest.getQueryString());
            }
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.sendRedirect(newUrl.toString());
        
        }	else if (chain!=null) chain.doFilter(request, response);
		
	}

	/**
	 * Return the filter configuration object for this filter.
	 */
	public FilterConfig getFilterConfig() {
		return (this.filterConfig);
	}


	/**
	 * Init method for this filter
	 */
	public void init(FilterConfig filterConfig) {		
		this.filterConfig = filterConfig;
	}

	
}
