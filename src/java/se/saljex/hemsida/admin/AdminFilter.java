/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.saljex.hemsida.admin;

import javax.servlet.FilterConfig;

/**
 *
 * @author Ulf
 */
public class AdminFilter extends se.saljex.loginservice.LoginServiceFilter{
	
	private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
	// this value is null, this filter instance is not currently
	// configured. 
	private FilterConfig filterConfig = null;
	
	public AdminFilter() {
		super();
	}	
	
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain)
//			throws IOException, ServletException {
//		super.doFilter(request,response,chain);
//		chain.doFilter(request, response);
//	}	
}
