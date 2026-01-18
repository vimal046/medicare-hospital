package com.hospital.medicare.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws ServletException,
			IOException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		String redirectUrl = "/";

		for (GrantedAuthority authority : authorities) {

			String role = authority.getAuthority();

			if (role.equals("ROLE_ADMIN")) {
				redirectUrl = "/admin/dashboard";
				break;
			} else if (role.equals("ROLE_DOCTOR")) {
				redirectUrl = "/doctor/dashboard";
				break;
			} else if (role.equals("ROLE_PATIENT")) {
				redirectUrl = "/patient/dashboard";
				break;
			}
		}
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
