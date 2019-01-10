package org.infrasoft.security.oauth2.CustomSecurityFilters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

public class CustomFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			System.out.println("-------------AUTHENTICATED----------");
			System.out.println(new UrlPathHelper().getPathWithinApplication((HttpServletRequest) request));
			System.out.println("------------------------------------");
		} else {
			System.out.println(new UrlPathHelper().getPathWithinApplication((HttpServletRequest) request));
		}
		chain.doFilter(request, response);
	}

}