package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.mybatis.LibraryMapper;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Spy
    @InjectMocks
    private LibraryService libraryServiceMock;

    private Wishlist wishlist;
    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        User currentUser = new User();
        wishlist = new Wishlist();
        book = new Book();

        wishlist.setId(1L);
        wishlist.setBooks(new HashSet<>());
        currentUser.setWishlist(wishlist);

        doReturn(currentUser).when(libraryServiceMock).getCurrentUser();
    }

    @Test
    public void addBookToWishlist_BookExistsInDb() throws Exception {
        String isbn = "1234567890";
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
        String isbn = "1234567890";
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
}
