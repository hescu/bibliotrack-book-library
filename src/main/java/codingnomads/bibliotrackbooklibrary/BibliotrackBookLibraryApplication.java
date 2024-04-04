package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.UserPrincipalMapper;
import codingnomads.bibliotrackbooklibrary.repository.security.AuthorityRepo;
import codingnomads.bibliotrackbooklibrary.repository.security.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class BibliotrackBookLibraryApplication implements CommandLineRunner {

	@Autowired
	private UserPrincipalRepo userPrincipalRepo;

	@Autowired
	private AuthorityRepo authorityRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserPrincipalMapper userPrincipalMapper;

	public static void main(String[] args) {
		SpringApplication.run(BibliotrackBookLibraryApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		if (userPrincipalMapper.findByUsername("admin").isEmpty()) {
			UserPrincipal adminUser = new UserPrincipal();
			Authority authority = authorityRepo.getReferenceById(2L);

			adminUser.setUsername("admin");
			adminUser.setPassword(passwordEncoder.encode("admin"));
			adminUser.setAuthorities(Collections.singletonList(authority));
			adminUser.setCredentialsNonExpired(true);
			adminUser.setAccountNonLocked(true);
			adminUser.setAccountNonExpired(true);
			adminUser.setEnabled(true);
			userPrincipalRepo.save(adminUser);
		}
	}
}
