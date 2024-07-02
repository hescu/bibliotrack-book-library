package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.IBookApi;
import codingnomads.bibliotrackbooklibrary.dao.SearchFormDataHolder;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SearchService {

    @Autowired
    private IBookApi googleBookApi;

    @Autowired
    private SearchFormDataHolder searchFormDataHolder;

    /**
     * Fetches search results from Google API or the cache.
     * @param searchFormData The {@link SearchFormData} from the user request.
     * @return A list of {@link Book}
     */
    @Cacheable(value = "searchResultsCache")
    public List<Book> performSearch(SearchFormData searchFormData) {

        if (Objects.equals(searchFormData.getSearchString(), "all")) {
            return googleBookApi.defaultSearch(searchFormData.getSearchString());
        } else {
            return googleBookApi.performSearch(searchFormData);
        }
    }
}
