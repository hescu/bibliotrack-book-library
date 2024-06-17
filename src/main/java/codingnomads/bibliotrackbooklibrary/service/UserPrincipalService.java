package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.exception.UserExceptions;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.UserPrincipalMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookshelfRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.security.AuthorityRepo;
import codingnomads.bibliotrackbooklibrary.repository.security.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    UserRepo userRepo;

    @Autowired
    BookshelfRepo bookshelfRepo;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipalRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or email not found : " + username));
    }

    /**
     * Create new user principal.
     *
     * @param userPrincipal the user principal
     */
    @Transactional
    public void createNewUserPrincipal(UserPrincipal userPrincipal) {
        checkUsername(userPrincipal.getUsername());
        checkPassword(userPrincipal.getPassword());
        userPrincipal.setId(null);
        userPrincipal.setAccountNonExpired(true);
        userPrincipal.setAccountNonLocked(true);
        userPrincipal.setCredentialsNonExpired(true);
        userPrincipal.setEnabled(true);

        Optional<Authority> auth = authorityRepo.findById(2L);
        auth.ifPresent(authority -> userPrincipal
                .setAuthorities(Collections
                .singletonList(authority))
        );

        userPrincipal.setPassword(passwordEncoder.encode(userPrincipal.getPassword()));

        try {
            User newUser = new User();
            newUser.setWishlist(new Wishlist());

            userPrincipal.setUser(newUser);
            userPrincipalRepo.save(userPrincipal);

            Bookshelf newBookshelf = new Bookshelf();
            newBookshelf.setUser(newUser);
            newBookshelf.setBooks(new HashSet<>());
            Bookshelf savedBookshelf = bookshelfRepo.save(newBookshelf);

            List<Bookshelf> bookshelves = new ArrayList<>();
            bookshelves.add(savedBookshelf);
            newUser.setBookshelves(bookshelves);

            userRepo.save(newUser);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }


    private User createNewUser() {
        User newUser = new User();
        Bookshelf newBookshelf = new Bookshelf();
        newUser.getBookshelves().add(newBookshelf);
        newBookshelf.setUser(newUser);
        try {
            bookshelfRepo.save(newBookshelf);
            userRepo.save(newUser);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
        return newUser;
    }

    /**
     * Find all users list.
     *
     * @return the list
     */
    public List<UserPrincipal> findAllUsers() {
        return userPrincipalRepo.findAll();
    }

    /**
     * Delete user by id.
     *
     * @param id the id
     */
    @Transactional
    public void deleteUserById(Long id) {
        int rowsDeleted = userPrincipalMapper.deleteFromUserAuthorityJoinTableById(id);
        int rowsDeleted2 = userPrincipalMapper.deleteUserPrincipalById(id);
        System.out.println(rowsDeleted + rowsDeleted2);
    }

    private void checkPassword(String password) {
        if(password == null) {
            throw new UserExceptions.InvalidPasswordException("You must set a password");
        }
        if (password.length() < 6 || password.length() > 30) {
            throw new UserExceptions.InvalidPasswordException("Password must be between 6 and 30 characters long");
        }
    }

    private void checkUsername(String username) {
        if (username == null) {
            throw new UserExceptions.InvalidUsernameException("You must enter a username");
        }
        if (username.length() > 70) {
            throw new UserExceptions.InvalidUsernameException("Username too long. Username cannot be longer than 70 characters.");
        }
        if (userPrincipalMapper.countUsernames(username) > 0) {
            throw new UserExceptions.UsernameAlreadyExistsException(username);
        }
    }
}
