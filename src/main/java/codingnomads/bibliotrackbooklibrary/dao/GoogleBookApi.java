package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.google.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class GoogleBookApi implements IBookApi {

    private final String ENDPOINT_BASE_URL = "https://www.googleapis.com";
    private final String ENDPOINT_GOOGLE_SEARCH = ENDPOINT_BASE_URL + "/books/v1/volumes?startIndex=%s&maxResults=%s&q=%s:%s&key=%s";
    public static final Map<String, String> searchCriteriaToGoogleQueryCriteria = new HashMap<>();

    private final int ITEMS_PER_PAGE = 10;

    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public GoogleBooksApiResponse performSearch(String searchText, String searchCriteria, int page) {
        int maxResults = 10;
        int startIndex = (page - 1) * maxResults;
        GoogleBooksApiResponse resultResponse = new GoogleBooksApiResponse();
        List<Item> resultItems = new ArrayList<>();
        String requestUrl = buildGoogleRequestUrl(searchText, searchCriteria, startIndex, maxResults);

        GoogleBooksApiResponse jsonResponse = restTemplate.getForObject(requestUrl, GoogleBooksApiResponse.class);
        if (jsonResponse != null && jsonResponse.getTotalItems() > 0) {
            resultItems = jsonResponse.getItems();
            for (Item item : resultItems) {
                System.out.println(item.getVolumeInfo().toString());
            }
            return jsonResponse;
        } else {
            return resultResponse;
        }
    }

    private String buildGoogleRequestUrl(String searchText, String searchCriteria, int startIndex, int maxResults) {
        searchCriteriaToGoogleQueryCriteria.put("author", "inauthor");
        searchCriteriaToGoogleQueryCriteria.put("title", "intitle");
        String searchTerm = searchCriteriaToGoogleQueryCriteria.get(searchCriteria);

        return String.format(ENDPOINT_GOOGLE_SEARCH, startIndex, maxResults, searchTerm, searchText, googleBooksApiKey);
    }
}
