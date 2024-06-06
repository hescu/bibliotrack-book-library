package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.dao.PostReviewApi;
import codingnomads.bibliotrackbooklibrary.model.*;
import codingnomads.bibliotrackbooklibrary.model.forms.ReviewForm;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.LibraryMapper;
import codingnomads.bibliotrackbooklibrary.mybatis.UserMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.BookshelfRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
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

    @Autowired
    private PostReviewApi postReviewApi;

    /**
     * Add book to wishlist.
     *
     * @param isbn the book isbn
     * @throws Exception if book can't be added to wishlist
     */
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

    /**
     * Remove book from wishlist.
     *
     * @param bookId the book id
     * @return the wishlist
     */
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

    /**
     * Fetch books from wishlist.
     *
     * @return the set of books
     */
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

    /**
     * Fetch bookshelves list.
     *
     * @return the list
     */
    public List<Bookshelf> fetchBookshelves() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            List<Bookshelf> currentUserBookshelves = new ArrayList<>();
            System.out.println("GETTING ALL BOOKSHELVES FOR USER");
            List<Long> bookshelfIds = libraryMapper.findAllBookshelvesIDsByUserId(currentUser.getId());
            for (Long id : bookshelfIds) {
                currentUserBookshelves.add(bookshelfRepo.getReferenceById(id));
            }
            return currentUserBookshelves;
        }
        return new ArrayList<>();
    }

    /**
     * Add book to bookshelf.
     *
     * @param isbn        the isbn
     * @param bookshelfId the bookshelf id
     */
    public void addBookToBookshelf(String isbn, Long bookshelfId) {
        try {
            Book bookFromDb = libraryMapper.findBookByIsbn(isbn);
            if (bookFromDb == null) {
                Book book = googleBookApi.searchBookByIsbn(isbn);
                System.out.println("BOOK FROM DB IS NULL. GOOGLE SEARCH BOOK ISBN: " + book.getIsbn());
                addBookToDB(book);
                Book newlyAddedBook = libraryMapper.findBookByIsbn(isbn);
                libraryMapper.addBookBookshelfRelation(newlyAddedBook.getId(), bookshelfId);
            } else {
                System.out.println("BOOK FROM DB - ISBN: " + bookFromDb.getIsbn());
                libraryMapper.addBookBookshelfRelation(bookFromDb.getId(), bookshelfId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Remove book from bookshelf.
     *
     * @param bookshelfId the bookshelf id
     * @param bookId      the book id
     */
    public Bookshelf removeBookFromBookshelf(Long bookshelfId, Long bookId) {
        try {
            libraryMapper.removeBookBookshelfRelation(bookshelfId, bookId);
            Optional<Bookshelf> optionalBookshelf = bookshelfRepo.findById(bookshelfId);
            return optionalBookshelf.orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove book from bookshelf: " + e.getMessage());
        }
    }

    public void postReview(ReviewForm reviewForm) {
        try {
            postReviewApi.postReview(reviewForm);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void addBookToDB(Book book) {
        libraryMapper.addBookToDB(book);
        for (Author author : book.getAuthors()) {
            libraryMapper.addAuthorToDB(author);
            libraryMapper.addAuthorBookRelation(author.getId(), book.getId());
        }
    }
}
