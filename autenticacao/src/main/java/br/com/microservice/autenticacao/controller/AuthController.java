package br.com.microservice.autenticacao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.autenticacao.data.vo.UserVO;
import br.com.microservice.autenticacao.jwt.JwtTokenProvider;
import br.com.microservice.autenticacao.service.UserService;

@RestController
@RequestMapping("login")
public class AuthController {

	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	private UserService userService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<Map<Object, Object>> login(@RequestBody UserVO userVO) {
		try {
			var userName = userVO.getUserName();
			var password = userVO.getPassword();

			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

			var user = this.userService.findByUserName(userName);
			var token = "";
			if (user != null) {
				token = this.jwtTokenProvider.createToken(userName, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Usuário não encontrado");
			}
			Map<Object, Object> map = new HashMap<>();
			map.put("username", userName);
			map.put("token", token);

			return ResponseEntity.ok().body(map);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Usuário e/ou senha inválido!");
		}
	}

	@RequestMapping("/testeSecurity")
	public String testeSecurity() {
		return "teste";
	}

}
