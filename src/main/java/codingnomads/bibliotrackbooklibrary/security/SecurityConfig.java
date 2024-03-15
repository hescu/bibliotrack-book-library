package codingnomads.bibliotrackbooklibrary.security;

import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import codingnomads.bibliotrackbooklibrary.model.security.RoleEnum;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/css/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers( "/", "/login", "/register").permitAll()
                    .anyRequest().authenticated())
            .formLogin(formLogin ->
                    formLogin
                            .loginPage("/login")
                            .permitAll()
                            .defaultSuccessUrl("/search"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        Authority userAuth = Authority.builder().authority(RoleEnum.ROLE_USER).build();
//        UserPrincipal user1 = new UserPrincipal("user", passwordEncoder().encode("resu"), Collections.singletonList(userAuth));
//        return new InMemoryUserDetailsManager(user1);
//    }
}
