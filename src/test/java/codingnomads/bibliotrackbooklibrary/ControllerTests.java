package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.controller.SearchController;
import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BibliotrackBookLibraryApplication.class
)
@AutoConfigureMockMvc(addFilters = false)
@Profile("test")
public class ControllerTests {

    @Mock
    private SearchService searchService;

    @Mock
    private View view;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void performSearch_Success() throws Exception {
        String searchText = "test";
        String searchCriteria = "default";

        SearchFormData searchFormData = SearchFormData.builder()
                .searchString(searchText)
                .searchCriteria(searchCriteria)
                .build();

        ThymeleafBook thymeleafBook = ThymeleafBook.builder()
                        .isbn("")
                        .authors(List.of(""))
                        .description("")
                        .title("")
                        .thumbnail("")
                        .publishedDate("")
                        .publisher("")
                        .build();

        List<ThymeleafBook> searchResults = new ArrayList<>();
        searchResults.add(thymeleafBook);

        when(searchService.performSearch(searchFormData)).thenReturn(searchResults);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                        .setSingleView(view)
                        .build();

        mockMvc.perform(post("/search")
                        .flashAttr("searchFormData", searchFormData))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists("searchResults"))
                        .andExpect(view().name("search"));

        verify(searchService).performSearch(searchFormData);
    }
}
