package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.model.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.response.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    @Autowired
    RestTemplate restTemplate;

    public List<Item> performSearch(String searchText) {
        List<Item> results = new ArrayList<>();
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchText + "&key=" + googleBooksApiKey;

        GoogleBooksApiResponse jsonResponse = restTemplate.getForObject(apiUrl, GoogleBooksApiResponse.class);
        if (jsonResponse != null && jsonResponse.getTotalItems() > 0) {
            return jsonResponse.getItems();
        } else {
            return results;
        }
    }
}
