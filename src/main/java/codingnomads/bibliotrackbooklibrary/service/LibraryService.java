package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.UserMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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

    public void addBookToWishlist(ThymeleafBook thymeleafBook) {
        Book book = convertToBookEntity(thymeleafBook);
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Wishlist wishlist = currentUser.getWishlist();
            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setBooks(new HashSet<>());
                currentUser.setWishlist(wishlist);
            }
            wishlist.getBooks().add(book);
            userRepo.save(currentUser);
        }
    }

    private Book convertToBookEntity(ThymeleafBook thymeleafBook) {
        Book book = new Book();
        book.setIsbn(thymeleafBook.getIsbn());
        book.setTitle(thymeleafBook.getTitle());
        book.setAuthors(thymeleafBook.getAuthors());
        book.setThumbnail(thymeleafBook.getThumbnail());
        book.setPublisher(thymeleafBook.getPublisher());
        book.setPublishedDate(thymeleafBook.getPublishedDate());
        book.setDescription(thymeleafBook.getDescription());
        book.setPageCount(thymeleafBook.getPageCount());

        return book;
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
}
