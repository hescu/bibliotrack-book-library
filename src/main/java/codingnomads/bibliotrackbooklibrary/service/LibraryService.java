package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private UserRepo userRepo;

    public void addBookToWishlist(ThymeleafBook thymeleafBook, String username) {
        Book book = convertToBookEntity(thymeleafBook);
        User currentUser =
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
}
