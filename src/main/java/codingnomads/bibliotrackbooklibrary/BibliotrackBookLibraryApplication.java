package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class BibliotrackBookLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotrackBookLibraryApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
