package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.IBookApi;
import codingnomads.bibliotrackbooklibrary.entity.google.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;
import codingnomads.bibliotrackbooklibrary.logging.Loggable;
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

    @Loggable
    public GoogleBooksApiResponse performSearch(String searchText, String searchCriteria, int page) {
        return googleBookApi.performSearch(searchText, searchCriteria, page);
    }

    public int calculateTotalPages(int totalItems, int itemsPerPage) {
        return (int) Math.ceil((double) totalItems / itemsPerPage);
    }
}
