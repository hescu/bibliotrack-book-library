package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.IBookApi;
import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private IBookApi googleBookApi;

    @Cacheable(value = "searchResultsCache")
    public List<ThymeleafBook> performSearch(SearchFormData searchFormData) {
        return googleBookApi.performSearch(searchFormData);
    }

    public int calculateTotalPages(int totalItems, int itemsPerPage) {
        return (int) Math.ceil((double) totalItems / itemsPerPage);
    }
}
