package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.model.response.ImageLinks;
import codingnomads.bibliotrackbooklibrary.model.response.Item;
import codingnomads.bibliotrackbooklibrary.model.response.VolumeInfo;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BibliotrackBookLibraryApplication.class
)
@AutoConfigureMockMvc(addFilters = false)
@Profile("test")
public class AppControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    public void indexHealthCheck() throws Exception {
        mockMvc
                .perform(
                        get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void performSearch_Success() throws Exception {
        String searchText = "test";
        String searchCriteria = "default";
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

        when(searchService.performSearch(searchText, searchCriteria)).thenReturn(searchResults);

        mockMvc.perform(post("/search")
                .param("searchText", searchText)
                .param("searchCriteria", searchCriteria))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchResults", searchResults));

        verify(searchService, times(1)).performSearch(searchText, searchCriteria);
    }
}
