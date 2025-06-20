package com.xp.store.security;

import com.xp.store.utils.URIs;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecurityUtils extends OncePerRequestFilter {

	private final String fixedToken;
	private final String secretKey;
	private final ArrayList<String> emptyValidate = new ArrayList<>(List.of("/api-docs"));
	private final ArrayList<String> fixedTokenValidate = new ArrayList<>(List.of(
			URIs.uri_login
	));

	public SecurityUtils(String fixedToken,String secret) {
		this.fixedToken = fixedToken;
		this.secretKey = secret;
	}

	protected void doFilterInternal(HttpServletRequest request,
									@NonNull HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		// Redireciona para o Swagger
		if(Objects.equals(requestURI, "/")){
			response.sendRedirect("/swagger-ui/index.html");
			return;
		}
		// Endpoints abertos
		if (emptyValidate.contains(requestURI)) {
			setAuthentication("open", request);

			filterChain.doFilter(request, response);
			return;
		}

		// Validação do token fixo
		if (fixedTokenValidate.contains(requestURI)) {
			String key = parseToken(request);
			if (key == null || !key.equals(fixedToken)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write("{\"message\": \"O Token informado é inválido ou expirou!\"}");
				response.getWriter().flush();
				return;
			}
			setAuthentication(key, request);
			filterChain.doFilter(request, response);
			return;
		}else {
			String token = parseToken(request);
			if (token == null || !validateJwtToken(token)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write("{\"message\": \"O Token informado é inválido ou expirou!\"}");
				response.getWriter().flush();
				return;
			}
			String username = extractUsername(token);
			setAuthentication(username, request);
		}
		filterChain.doFilter(request, response);
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}

	private boolean validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String extractUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	private void setAuthentication(String user, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				user, null, null);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
