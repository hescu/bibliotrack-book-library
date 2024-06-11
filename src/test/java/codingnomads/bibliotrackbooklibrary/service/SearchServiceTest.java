package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.model.Author;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Profile("test")
@TestPropertySource(properties = {
        "spring.cache.type=none" // Deactivates chaching for tests
})
class SearchServiceTest {

    @Mock
    private GoogleBookApi googleBookApi;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testPerformSearch() {
        SearchFormData searchFormData = SearchFormData.builder()
                .searchString("test")
                .searchCriteria("author")
                .startIndex(0)
                .maxResults(40)
                .build();

        Author author1 = new Author("author1");
        Author author2 = new Author("author2");

        Book book1 = Book.builder()
                .isbn("123")
                .title("title1")
                .authors(List.of(author1, author2))
                .thumbnail("")
                .publisher("")
                .publishedDate("")
                .description("")
                .pageCount(666)
                .build();

        Book book2 = Book.builder()
                .isbn("321")
                .title("title2")
                .authors(List.of(author1, author2))
                .thumbnail("")
                .publisher("")
                .publishedDate("")
                .description("")
                .pageCount(555)
                .build();

        List<Book> expectedBooks = Arrays.asList(book1, book2);

        when(googleBookApi.performSearch(any(SearchFormData.class))).thenReturn(expectedBooks);

        // Act
        List<Book> result1 = searchService.performSearch(searchFormData);
        List<Book> result2 = searchService.performSearch(searchFormData); // Should hit the cache

        // Assert
        assertThat(result1).isEqualTo(expectedBooks);
        assertThat(result2).isEqualTo(expectedBooks);

        // Verify that the googleBookApi.performSearch was called only once
        verify(googleBookApi, times(1)).performSearch(any(SearchFormData.class));
    }
}