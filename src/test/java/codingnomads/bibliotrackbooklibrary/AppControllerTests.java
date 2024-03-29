package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
