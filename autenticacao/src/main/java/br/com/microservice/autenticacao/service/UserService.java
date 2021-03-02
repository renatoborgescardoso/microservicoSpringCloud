package br.com.microservice.autenticacao.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.microservice.autenticacao.entity.User;
import br.com.microservice.autenticacao.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException(MessageFormat.format("Usuário não encontrado. ({0})", username));
		}
		return user;
	}

}
