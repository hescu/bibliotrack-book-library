package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.model.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.response.SearchKeyword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class AppController {

    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    @GetMapping
    public String displayIndex(Model model) {
        model.addAttribute("searchKeyword", new SearchKeyword());
        return "index";
    }

    @PostMapping("/search")
    public String performSearch(@ModelAttribute("searchKeyword") SearchKeyword searchKeyword, Model model) throws JsonProcessingException {
        String searchText = searchKeyword.getText();

        // URL für die Google Books API-Anfrage
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchText + "&key=" + googleBooksApiKey;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // Konvertiere das JSON in ein GoogleBooksApiResponse-Objekt
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleBooksApiResponse response = objectMapper.readValue(jsonResponse, GoogleBooksApiResponse.class);

        model.addAttribute("searchResults", response.getItems());
        return "index";
    }
}
