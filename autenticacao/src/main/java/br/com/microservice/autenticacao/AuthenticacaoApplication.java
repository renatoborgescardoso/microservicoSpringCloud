package br.com.microservice.autenticacao;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.microservice.autenticacao.entity.Permission;
import br.com.microservice.autenticacao.entity.User;
import br.com.microservice.autenticacao.repository.PermissionRepository;
import br.com.microservice.autenticacao.repository.UserRepository;

@SpringBootApplication
public class AuthenticacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticacaoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PermissionRepository permissionRepository,
			BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, permissionRepository, passwordEncoder);
		};
	}

	private void initUsers(UserRepository userRepository, PermissionRepository permissionRepository,
			BCryptPasswordEncoder passwordEncoder) {
		Permission permission = null;
		Permission findPermission = permissionRepository.findByDescription("Administrador");
		if (findPermission == null) {
			permission = new Permission();
			permission.setDescription("Administrador");
			permission = permissionRepository.save(permission);
		} else {
			permission = findPermission;
		}
		
		User user = new User();
		user.setUserName("admin");
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("admin"));
		User findUser = userRepository.findByUserName("admin");
		if (findUser == null) {
			userRepository.save(user);
		}
	}
}
