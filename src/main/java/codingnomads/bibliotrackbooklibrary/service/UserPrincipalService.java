package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.repository.security.AuthorityRepo;
import codingnomads.bibliotrackbooklibrary.repository.security.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserPrincipalRepo userPrincipalRepo;

    @Autowired
    AuthorityRepo authorityRepo;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipalRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or email not found : " + username));
    }

    public UserPrincipal createNewUser(UserPrincipal userPrincipal) {
        userPrincipal.setId(null);
        userPrincipal.setAccountNonExpired(true);
        userPrincipal.setAccountNonLocked(true);
        userPrincipal.setCredentialsNonExpired(true);
        userPrincipal.setEnabled(true);

        Optional<Authority> auth = authorityRepo.findById(1L);
        auth.ifPresent(authority -> userPrincipal
                .setAuthorities(Collections
                .singletonList(authority))
        );

        checkPassword(userPrincipal.getPassword());
        userPrincipal.setPassword(passwordEncoder.encode(userPrincipal.getPassword()));
        try {
            return userPrincipalRepo.save(userPrincipal);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password) {
        if(password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if(password.length() < 8) {
            throw new IllegalStateException("Password is too short. Must be larger than 6 characters");
        }
    }
}
