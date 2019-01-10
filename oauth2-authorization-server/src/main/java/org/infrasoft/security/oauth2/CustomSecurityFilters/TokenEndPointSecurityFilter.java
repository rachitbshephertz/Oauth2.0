package org.infrasoft.security.oauth2.CustomSecurityFilters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.web.util.UrlPathHelper;

public class TokenEndPointSecurityFilter implements Filter {
	
	private static final String AUTHORIZED_CLIENT_IP = "172.25.1.156";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("URL IS :" + new UrlPathHelper().getPathWithinApplication((HttpServletRequest) request));
		System.out.println(request.getRemoteAddr());
		if (!AUTHORIZED_CLIENT_IP.equals(request.getRemoteAddr())) { 
            //throw new BadClientCredentialsException();
        } 
		long timeLapse = Instant.now().getEpochSecond() - Long.valueOf(request.getParameterValues("timestamp")[0]);
		if (timeLapse>300 && timeLapse>0 ){
			//throw new BadClientCredentialsException();
		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	

}
