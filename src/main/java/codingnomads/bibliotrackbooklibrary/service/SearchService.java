package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.model.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.response.Item;
import codingnomads.bibliotrackbooklibrary.model.response.VolumeInfo;
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

    public List<Item> performSearch(String searchText, String searchCriteria) {
        List<Item> results = new ArrayList<>();
        String requestUrl = buildRequestUrl(searchText, searchCriteria);

        GoogleBooksApiResponse jsonResponse = restTemplate.getForObject(requestUrl, GoogleBooksApiResponse.class);
        if (jsonResponse != null && jsonResponse.getTotalItems() > 0) {
            results = jsonResponse.getItems();
            for (Item item : results) {
                System.out.println(item.getVolumeInfo().toString());
            }
            return jsonResponse.getItems();
        } else {
            return results;
        }
    }

    private String buildRequestUrl(String searchText, String searchCriteria) {
        String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";

        return switch (searchCriteria) {
            case "isbn" -> baseUrl + "isbn:" + searchText + "&key=" + googleBooksApiKey;
            case "author" -> baseUrl + "inauthor:" + searchText + "&key=" + googleBooksApiKey;
            case "title" -> baseUrl + "intitle:" + searchText + "&key=" + googleBooksApiKey;
            default -> baseUrl + searchText + "&key=" + googleBooksApiKey;
        };
    }
}
