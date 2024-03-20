package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.entity.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.response.ImageLinks;
import codingnomads.bibliotrackbooklibrary.entity.response.Item;
import codingnomads.bibliotrackbooklibrary.entity.response.VolumeInfo;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BibliotrackBookLibraryApplication.class
)
@AutoConfigureMockMvc(addFilters = false)
@Profile("test")
public class SearchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    public void performSearch_Success() throws Exception {
        String searchText = "test";
        String searchCriteria = "default";
        int page = 1;
        GoogleBooksApiResponse testGoogleBooksApiResponse = new GoogleBooksApiResponse();

        Item item = Item.builder()
                .volumeInfo(VolumeInfo.builder()
                        .authors(List.of(""))
                        .description("")
                        .title("")
                        .imageLinks(ImageLinks.builder()
                                .thumbnail("")
                                .build())
                        .publishedDate("")
                        .publisher("")
                        .build())
                .build();
        List<Item> searchResults = new ArrayList<>();
        searchResults.add(item);
        testGoogleBooksApiResponse.setItems(searchResults);

        when(searchService.performSearch(searchText, searchCriteria, page)).thenReturn(testGoogleBooksApiResponse);
        when(searchService.calculateTotalPages(1, 10)).thenReturn(1);

        mockMvc.perform(get("/search/google-books-api")
                        .param("searchText", searchText)
                        .param("searchCriteria", searchCriteria))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItemsFound", testGoogleBooksApiResponse.getTotalItems()))
                .andExpect(model().attribute("searchResults", searchResults))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", 1));

        verify(searchService, times(1)).performSearch(searchText, searchCriteria, page);
    }
}
