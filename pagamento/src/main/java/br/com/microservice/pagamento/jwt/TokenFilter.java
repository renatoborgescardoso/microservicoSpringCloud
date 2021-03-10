package br.com.microservice.pagamento.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class TokenFilter extends GenericFilterBean {

	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	public TokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

		if (token != null && this.jwtTokenProvider.validateToken(token)) {
			Authentication auth = jwtTokenProvider.getAuthentication(token);
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth); 
			}
		}
		chain.doFilter(request, response);
	}

}
