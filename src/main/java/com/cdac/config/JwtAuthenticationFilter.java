package com.cdac.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cdac.entity.Employee;
import com.cdac.service.EmployeeService;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	public JwtAuthenticationFilter(EmployeeService employeeService, JwtTokenHelper jwtTokenHelper) {
		this.employeeService = employeeService;
		this.jwtTokenHelper = jwtTokenHelper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authToken;
		authToken = jwtTokenHelper.getToken(request);	
		if(authToken != null) {
			String userName = jwtTokenHelper.getUsernameFromToken(authToken);
			if(userName != null) {
				UserDetails user = (UserDetails) employeeService.login(userName, authToken);
				if(jwtTokenHelper.validateToken(authToken, user)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
