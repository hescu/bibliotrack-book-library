package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.dao.PostReviewApi;
import codingnomads.bibliotrackbooklibrary.exception.BookNotFoundException;
import codingnomads.bibliotrackbooklibrary.model.*;
import codingnomads.bibliotrackbooklibrary.model.forms.ReviewForm;
import codingnomads.bibliotrackbooklibrary.mybatis.LibraryMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookshelfRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.cache.type=none" // Deactivates chaching for tests
})
public class LibraryServiceTest {

    @Mock
    private LibraryMapper libraryMapperMock;

    @Mock
    private GoogleBookApi googleBookApiMock;

    @Mock
    private WishlistRepo wishlistRepoMock;

    @Mock
    private BookshelfRepo bookshelfRepoMock;

    @Mock
    private PostReviewApi postReviewApiMock;

    @Spy
    @InjectMocks
    private LibraryService libraryServiceMock;

    private User currentUser;
    private Wishlist wishlist;
    private Book book;
    private Bookshelf bookshelf1;
    private Bookshelf bookshelf2;
    private String isbn;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        isbn = "1234567890";
        currentUser = new User();
        wishlist = new Wishlist();
        book = new Book();
        book.setIsbn("1234567890");
        book.setId(1L);

        bookshelf1 = new Bookshelf();
        bookshelf1.setId(1L);

        bookshelf2 = new Bookshelf();
        bookshelf2.setId(2L);

        wishlist.setId(1L);
        wishlist.setBooks(new HashSet<>());
        currentUser.setWishlist(wishlist);

        lenient().doReturn(currentUser).when(libraryServiceMock).getCurrentUser();
    }

    @Test
    public void addBookToWishlist_BookExistsInDb() throws Exception {
        when(libraryMapperMock.findBookByIsbn(isbn)).thenReturn(book);
        when(wishlistRepoMock.save(any(Wishlist.class))).thenReturn(wishlist);
        when(libraryServiceMock.getCurrentUserWishlist(anyLong())).thenReturn(wishlist);

        libraryServiceMock.addBookToWishlist(isbn);

        verify(libraryMapperMock, times(1)).findBookByIsbn(isbn);
        verify(googleBookApiMock, times(0)).searchBookByIsbn(anyString());
        verify(wishlistRepoMock, times(1)).save(wishlist);
    }

    @Test
    public void addBookToWishlist_BookNotInDb() throws Exception {
        when(libraryMapperMock.findBookByIsbn(isbn)).thenReturn(null);
        when(googleBookApiMock.searchBookByIsbn(isbn)).thenReturn(book);
        when(wishlistRepoMock.save(any(Wishlist.class))).thenReturn(wishlist);
        when(libraryServiceMock.getCurrentUserWishlist(anyLong())).thenReturn(wishlist);

        libraryServiceMock.addBookToWishlist(isbn);

        verify(libraryMapperMock, times(1)).findBookByIsbn(isbn);
        verify(googleBookApiMock, times(1)).searchBookByIsbn(isbn);
        verify(wishlistRepoMock, times(1)).save(wishlist);
    }

    @Test
    void removeBookFromWishlist_Success() {
        Long bookId = 1L;
        when(wishlistRepoMock.findById(anyLong())).thenReturn(Optional.of(wishlist));

        Wishlist result = libraryServiceMock.removeBookFromWishlist(bookId);

        verify(libraryMapperMock, times(1)).removeBookFromWishlist(wishlist.getId(), bookId);
        verify(wishlistRepoMock, times(1)).findById(wishlist.getId());
        assertEquals(result.getId(), wishlist.getId());
    }

    @Test
    public void removeBookFromWishlist_Failure() {
        Long bookId = 1L;
        when(wishlistRepoMock.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libraryServiceMock.removeBookFromWishlist(bookId);
        });

        assertTrue(exception.getMessage().contains("Failed to remove book from wishlist"));
        verify(libraryMapperMock, times(1)).removeBookFromWishlist(wishlist.getId(), bookId);
        verify(wishlistRepoMock, times(1)).findById(wishlist.getId());
    }

    @Test
    void fetchBookshelves_Success() {
        List<Long> bookshelfIds = Arrays.asList(1L, 2L);
        when(libraryMapperMock.findAllBookshelvesIDsByUserId(currentUser.getId())).thenReturn(bookshelfIds);
        when(bookshelfRepoMock.getReferenceById(1L)).thenReturn(bookshelf1);
        when(bookshelfRepoMock.getReferenceById(2L)).thenReturn(bookshelf2);

        List<Bookshelf> result = libraryServiceMock.fetchBookshelves();

        verify(libraryMapperMock, times(1)).findAllBookshelvesIDsByUserId(currentUser.getId());
        verify(bookshelfRepoMock, times(1)).getReferenceById(1L);
        verify(bookshelfRepoMock, times(1)).getReferenceById(2L);
        assertEquals(2, result.size());
        assertEquals(bookshelf1, result.get(0));
        assertEquals(bookshelf2, result.get(1));
    }

    @Test
    void addBookToBookshelf_BookExistsInDb() {
        Long bookshelfId = 1L;
        when(libraryMapperMock.findBookByIsbn(isbn)).thenReturn(book);

        libraryServiceMock.addBookToBookshelf(isbn, bookshelfId);

        verify(libraryMapperMock, times(1)).findBookByIsbn(isbn);
        verify(libraryMapperMock, times(1)).addBookBookshelfRelation(book.getId(), bookshelfId);
        verify(googleBookApiMock, times(0)).searchBookByIsbn(anyString());
    }

    @Test
    public void addBookToBookshelf_BookNotInDb() {
        Long bookshelfId = 1L;
        Book googleBook = new Book();
        googleBook.setIsbn(isbn);
        googleBook.setId(2L);

        List<Author> listOfAuthors = new ArrayList<>();
        googleBook.setAuthors(listOfAuthors);

        when(libraryMapperMock.findBookByIsbn(isbn))
                .thenReturn(null)
                .thenReturn(googleBook);

        when(googleBookApiMock.searchBookByIsbn(isbn)).thenReturn(googleBook);

        libraryServiceMock.addBookToBookshelf(isbn, bookshelfId);

        verify(libraryMapperMock, times(2)).findBookByIsbn(isbn);
        verify(googleBookApiMock, times(1)).searchBookByIsbn(isbn);
        verify(libraryServiceMock, times(1)).addBookToDB(googleBook);
        verify(libraryMapperMock, times(1)).addBookBookshelfRelation(anyLong(), eq(bookshelfId));
    }

    @Test
    void removeBookFromBookshelf_Success() {
        Long bookshelfId = 1L;
        Long bookId = 1L;
        when(bookshelfRepoMock.findById(bookshelfId)).thenReturn(Optional.of(bookshelf1));

        Bookshelf result = libraryServiceMock.removeBookFromBookshelf(bookshelfId, bookId);

        verify(libraryMapperMock, times(1)).removeBookBookshelfRelation(bookshelfId, bookId);
        verify(bookshelfRepoMock, times(1)).findById(bookshelfId);
        assertEquals(result.getId(), bookshelfId);
    }

    @Test
    public void removeBookFromBookshelf_Failure() {
        Long bookshelfId = 1L;
        Long bookId = 1L;
        when(bookshelfRepoMock.findById(bookshelfId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libraryServiceMock.removeBookFromBookshelf(bookshelfId, bookId);
        });

        assertTrue(exception.getMessage().contains("Failed to remove book from bookshelf"));
        verify(libraryMapperMock, times(1)).removeBookBookshelfRelation(bookshelfId, bookId);
        verify(bookshelfRepoMock, times(1)).findById(bookshelfId);
    }

    @Test
    void getBookFromDatabase_Success() {
        when(libraryMapperMock.findBookByIsbn(isbn)).thenReturn(book);

        libraryServiceMock.getBookFromDatabase(isbn);

        verify(libraryMapperMock, times(1)).findBookByIsbn(isbn);
    }

    @Test
    void getBookFromDatabase_Failure() {
        when(libraryMapperMock.findBookByIsbn(isbn)).thenReturn(null);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            libraryServiceMock.getBookFromDatabase(isbn);
        });

        assertTrue(exception.getMessage().contains("Book with ISBN " + isbn + " not found"));
        verify(libraryMapperMock, times(1)).findBookByIsbn(isbn);
    }

    @Test
    void postReview_Success() {
        ReviewForm reviewForm = ReviewForm.builder()
                .username("")
                .isbn("123")
                .rating(1)
                .review("Good Book.")
                .build();

        when(libraryServiceMock.getCurrentUserName()).thenReturn("username");
        when(postReviewApiMock.postReview(reviewForm)).thenReturn(true);

        libraryServiceMock.postReview(reviewForm);

        verify(libraryServiceMock, times(1)).getCurrentUserName();
        verify(postReviewApiMock, times(1)).postReview(reviewForm);
    }
}
