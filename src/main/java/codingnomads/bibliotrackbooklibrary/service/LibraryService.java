package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.LibraryMapper;
import codingnomads.bibliotrackbooklibrary.mybatis.UserMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.BookshelfRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private BookshelfRepo bookshelfRepo;

    @Transactional
    public void addBookToWishlist(String isbn) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                Wishlist wishlist = getCurrentUserWishlist(currentUser.getWishlist().getId());
                Book bookFromDb = libraryMapper.findBookByIsbn(isbn);
                if (bookFromDb != null) {
                    wishlist.getBooks().add(bookFromDb);
                } else {
                    wishlist.getBooks().add(googleBookApi.searchBookByIsbn(isbn));
                }
                wishlistRepo.save(wishlist);
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

    /**
     * Gets user from current context
     * @return The current user from context.
     */
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
        System.out.println("FETCHING BOOKS FROM WISHLIST");
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            System.out.println("CURRENT USER IS NOT NULL");
            Optional<Wishlist> wishlist = wishlistRepo.findById(currentUser.getWishlist().getId());
            if (wishlist.isPresent()) {
                System.out.println("WISHLIST IS PRESENT");
                for (Book b : wishlist.get().getBooks()) {
                    System.out.println("BOOK ISBN: " + b.getIsbn());
                }


                return wishlist.get().getBooks();
            }
        }

        System.out.println("RETURNING EMPTY SET FOR WISHLIST BOOKS");
        return new HashSet<>();
    }

    private Wishlist getCurrentUserWishlist(Long wishlistId) {
        Optional<Wishlist> optionalWishlist = wishlistRepo.findById(wishlistId);
        return optionalWishlist.orElse(null);
    }

    public List<Bookshelf> fetchBookshelves() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            System.out.println("GETTING ALL BOOKSHELVES FOR USER");
            return libraryMapper.findAllBookshelvesByUserId(currentUser.getId());
        }
        return new ArrayList<>();
    }

    public void addBookToBookshelf(String isbn, Long bookshelfId) {
        try {
//            Book bookFromDb = libraryMapper.findBookByIsbn(isbn);
//            if (bookFromDb != null) {
//                libraryMapper.addBookToBookshelf(bookFromDb.getId(), bookshelfId);
//            } else {
                Book book = googleBookApi.searchBookByIsbn(isbn);
                libraryMapper.addBookToDB(book);
                libraryMapper.addBookToBookshelf(book.getId(), bookshelfId);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeBookFromBookshelf(Long bookshelfId, Long bookId) {
        try {
            libraryMapper.removeBookFromBookshelf(bookshelfId, bookId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove book from bookshelf: " + e.getMessage());
        }
    }
}
