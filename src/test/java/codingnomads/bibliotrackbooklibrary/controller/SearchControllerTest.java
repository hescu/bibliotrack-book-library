package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.BibliotrackBookLibraryApplication;
import codingnomads.bibliotrackbooklibrary.cache.CacheService;
import codingnomads.bibliotrackbooklibrary.dao.SearchFormDataHolder;
import codingnomads.bibliotrackbooklibrary.model.Author;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import codingnomads.bibliotrackbooklibrary.configuration.SecurityConfig;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO: Use inheritance, e.g.:  a BaseTestClass

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {BibliotrackBookLibraryApplication.class, SecurityConfig.class}
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.cache.type=none" // Deactivates chaching for tests
})
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @MockBean
    private LibraryService libraryService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private SearchController searchController;

    @MockBean
    private SearchFormDataHolder searchFormDataHolder;

    private SearchFormData searchFormData;

    @BeforeEach
    public void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();

        searchFormData = SearchFormData.builder()
                .searchCriteria("all")
                .searchString("Some Author")
                .build();
        searchFormDataHolder.setSearchFormData(searchFormData);
    }

    @Test
    public void performSearch_Controller_Success() throws Exception {

        Author author = Author.builder()
                .name("Author")
                .build();

        List<Author> authorList = new ArrayList<>();
        authorList.add(author);

        Book book = Book.builder()
                        .isbn("")
                        .authors(authorList)
                        .description("")
                        .title("")
                        .thumbnail("")
                        .publishedDate("")
                        .publisher("")
                        .build();

        List<Book> searchResults = new ArrayList<>();
        searchResults.add(book);

        when(searchService.performSearch(searchFormData)).thenReturn(searchResults);
        when(libraryService.fetchBookshelves()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/search/")
                        .flashAttr("searchFormData", searchFormData))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists("searchResults"))
                        .andExpect(view().name("search"));

        verify(searchService).performSearch(any(SearchFormData.class));
    }

    @Test
    public void testAddBookToWishlistSuccess() throws Exception {
        List<Book> cachedSearchResults = new ArrayList<>();

        when(searchFormDataHolder.getSearchFormData()).thenReturn(searchFormData);
        when(cacheService.getCachedSearchResults(searchFormDataHolder.getSearchFormData())).thenReturn(cachedSearchResults);

        doNothing().when(libraryService).addBookToWishlist(any(String.class));

        mockMvc.perform(post("/search/wishlist/add")
                        .param("isbn", "9780192835031"))
                .andExpect(redirectedUrl("/search"))
                .andExpect(flash().attribute("searchResults", cachedSearchResults))
                .andExpect(flash().attribute("message", "Book added to wishlist successfully."))
                .andExpect(flash().attribute("alertClass", "alert-success"));
    }

    @Test
    public void testAddBookToWishlistFailure() throws Exception {
        List<Book> cachedSearchResults = new ArrayList<>();

        when(cacheService.getCachedSearchResults(searchFormDataHolder.getSearchFormData())).thenReturn(cachedSearchResults);
        doThrow(new RuntimeException("Book is already on your wishlist.")).when(libraryService).addBookToWishlist(anyString());

        mockMvc.perform(post("/search/wishlist/add")
                        .param("isbn", "9780192835031"))
                .andExpect(redirectedUrl("/search"))
                .andExpect(flash().attribute("searchResults", cachedSearchResults))
                .andExpect(flash().attribute("message", "Failed to add book to wishlist: Book is already on your wishlist."))
                .andExpect(flash().attribute("alertClass", "alert-danger"));
    }
}
