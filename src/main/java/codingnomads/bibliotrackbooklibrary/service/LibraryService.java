package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.LibraryMapper;
import codingnomads.bibliotrackbooklibrary.mybatis.UserMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private GoogleBookApi googleBookApi;

    @Transactional
    public void addBookToWishlist(String isbn) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                Wishlist wishlist = currentUser.getWishlist();
                wishlist.getBooks().add(googleBookApi.searchBookByIsbn(isbn));
                userRepo.save(currentUser);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public Wishlist removeBookFromWishlist(Long bookId) {
        try {
            Long wishlistId = Objects.requireNonNull(getCurrentUser()).getWishlist().getId();
            libraryMapper.removeBookFromWishlist(wishlistId, bookId);
            Optional<Wishlist> optionalWishlist = wishlistRepo.findById(wishlistId);
             return optionalWishlist.orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove book from wishlist: " + e.getMessage());
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserPrincipal userPrincipal) {
                return userPrincipal.getUser();
            }
        }
        return null;
    }

    public Set<Book> fetchBooksFromWishlist() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Optional<Wishlist> wishlist = wishlistRepo.findById(currentUser.getWishlist().getId());
            if (wishlist.isPresent()) {
                return wishlist.get().getBooks();
            }
        }
        return null;
    }
}
