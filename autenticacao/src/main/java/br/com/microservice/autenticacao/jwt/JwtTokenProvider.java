package br.com.microservice.autenticacao.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key}")
	private String secret;

	@Value("${security.jwt.token.expired}")
	private long expired;
	
	@Autowired
	@Qualifier("userService")
	private UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
	}
	
	
	public String createToken(String userName, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userName);
		claims.put("roles", roles);
		
		Date now = new Date();
		Date validateToken = new Date(now.getTime() + expired);
		
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validateToken)
				.signWith(SignatureAlgorithm.ES256, secret).compact();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserNameByToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	private String getUserNameByToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
