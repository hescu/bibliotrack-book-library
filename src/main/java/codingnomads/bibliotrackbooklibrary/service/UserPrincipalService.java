package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.exception.UserExceptions;
import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.UserPrincipalMapper;
import codingnomads.bibliotrackbooklibrary.repository.security.AuthorityRepo;
import codingnomads.bibliotrackbooklibrary.repository.security.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserPrincipalRepo userPrincipalRepo;

    @Autowired
    AuthorityRepo authorityRepo;

    @Autowired
    UserPrincipalMapper userPrincipalMapper;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipalRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or email not found : " + username));
    }

    public UserPrincipal createNewUser(UserPrincipal userPrincipal) {
        if (!checkIfUsernameAlreadyExists(userPrincipal.getUsername())) {
            throw new UserExceptions.UsernameAlreadyExistsException(userPrincipal.getUsername());
        }
        checkPassword(userPrincipal.getPassword());
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

        userPrincipal.setPassword(passwordEncoder.encode(userPrincipal.getPassword()));

        try {
            return userPrincipalRepo.save(userPrincipal);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password) {
        if(password == null) {
            throw new UserExceptions.InvalidPasswordException("You must set a password");
        }
        if (password.length() < 6 || password.length() > 30) {
            throw new UserExceptions.InvalidPasswordException("Password must be between 6 and 30 characters long");
        }
    }

    private boolean checkIfUsernameAlreadyExists(String username) {
        return userPrincipalMapper.countUsernames(username) == 0;
    }

    public List<UserPrincipal> findAllUsers() {
        return userPrincipalRepo.findAll();
    }
}
