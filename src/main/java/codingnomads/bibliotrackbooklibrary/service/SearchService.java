package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.IBookApi;
import codingnomads.bibliotrackbooklibrary.entity.google.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    @Autowired
    private IBookApi googleBookApi;

    public List<Item> performSearch(String searchText, String searchCriteria) {
        return googleBookApi.preformSearch(searchText, searchCriteria);
    }
}
