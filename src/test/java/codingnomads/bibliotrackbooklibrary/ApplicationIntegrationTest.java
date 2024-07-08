package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.dao.SearchFormDataHolder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ApplicationIntegrationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }

    @Test
    void hasSearchFormDataHolderConfigured(ApplicationContext context) {
        assertThat(context.getBean(SearchFormDataHolder.class)).isNotNull();
    }
}
