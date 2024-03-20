package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class GoogleBookApi implements IBookApi {

    private final String ENDPOINT_BASE_URL = "https://www.googleapis.com";
    private final String ENDPOINT_GOOGLE_SEARCH = ENDPOINT_BASE_URL + "/books/v1/volumes?maxResults=%s&q=%s:%s&key=%s";
    public static final Map<String, String> searchCriteriaToGoogleQueryCriteria = new HashMap<>();

    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public GoogleBooksApiResponse performSearch(SearchFormData searchFormData) {
        final int MAX_RESULTS = 40;
        GoogleBooksApiResponse resultResponse = new GoogleBooksApiResponse();
        String requestUrl = buildGoogleRequestUrl(
                searchFormData.getSearchString(),
                searchFormData.getSearchCriteria(),
                MAX_RESULTS
        );

        GoogleBooksApiResponse jsonResponse = restTemplate.getForObject(requestUrl, GoogleBooksApiResponse.class);
        if (jsonResponse != null && jsonResponse.getTotalItems() > 0) {
            return jsonResponse;
        } else {
            return resultResponse;
        }
    }

    private String buildGoogleRequestUrl(String searchText, String searchCriteria, int maxResults) {
        searchCriteriaToGoogleQueryCriteria.put("author", "inauthor");
        searchCriteriaToGoogleQueryCriteria.put("title", "intitle");
        String searchTerm = searchCriteriaToGoogleQueryCriteria.get(searchCriteria);

        return String.format(ENDPOINT_GOOGLE_SEARCH, maxResults, searchTerm, searchText, googleBooksApiKey);
    }
}
