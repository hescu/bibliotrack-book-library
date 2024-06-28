package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.response.Item;
import codingnomads.bibliotrackbooklibrary.model.response.VolumeInfo;
import codingnomads.bibliotrackbooklibrary.model.Author;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Google books API.
 */
@Component
@Getter
public class GoogleBookApi implements IBookApi {

    //TODO Optimize search

    private final String ENDPOINT_BASE_URL = "https://www.googleapis.com";
    private final String ENDPOINT_GOOGLE_SEARCH = ENDPOINT_BASE_URL + "/books/v1/volumes?startIndex=%s&maxResults=%s&langRestrict=%s&q=%s:%s&key=%s";
    private final String LANGUAGE_RESTRICTION = "en";

    public static final Map<String, String> searchCriteriaToGoogleQueryCriteria = new HashMap<>();

    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Book> performSearch(SearchFormData searchFormData) {
        String requestUrl = buildGoogleRequestUrl(
                searchFormData.getSearchString(),
                searchFormData.getSearchCriteria(),
                searchFormData.getStartIndex(),
                searchFormData.getMaxResults()
        );

        return sendRequest(requestUrl);
    }

    @Override
    public List<Book> defaultSearch(String searchString) {
        final String ENDPOINT_DEFAULT_SEARCH = ENDPOINT_BASE_URL + "/books/v1/volumes?q=%s&key=%s";
        String requestUrl = String.format(ENDPOINT_DEFAULT_SEARCH, searchString, googleBooksApiKey);
        List<Book> foundBooks = sendRequest(requestUrl);
        return Objects.requireNonNullElseGet(foundBooks, ArrayList::new);
    }

    @Override
    public Book searchBookByIsbn(String isbn) {
        final String ENDPOINT_ISBN_SEARCH = ENDPOINT_BASE_URL + "/books/v1/volumes?q=isbn:%s&key=%s";
        String requestUrl = String.format(ENDPOINT_ISBN_SEARCH, isbn, googleBooksApiKey);
        List<Book> foundBooks = sendRequest(requestUrl);
        if (foundBooks != null) {
            return foundBooks.getFirst();
        } else {
            return null;
        }
    }

    /** Parse VolumeInfo to Book
     *
     * @param volumeInfo VolumeInfo from search
     * @return Book
     */
    private static Book parseVolumeInfoToBook(VolumeInfo volumeInfo) {
        String thumbnail = null;
        if (volumeInfo.getImageLinks() != null) {
            thumbnail = volumeInfo.getImageLinks().getThumbnail();
        }

        String isbn = null;
        if (volumeInfo.getIndustryIdentifiers() != null) {
            isbn = volumeInfo.getIndustryIdentifiers().getFirst().getIdentifier();
        }

        List<Author> authorList = getAuthorList(volumeInfo);

        return new Book(
                isbn,
                volumeInfo.getTitle(),
                authorList,
                thumbnail,
                volumeInfo.getPublisher(),
                volumeInfo.getPublishedDate(),
                volumeInfo.getDescription(),
                volumeInfo.getPageCount()
        );
    }

    private static List<Author> getAuthorList(VolumeInfo volumeInfo) {
        List<Author> authorList = new ArrayList<>();
        if (volumeInfo.getAuthors() != null) {
            for (String authorName : volumeInfo.getAuthors()) {
                authorList.add(new Author(authorName));
            }
        }
        return authorList;
    }

    /** Build URL for Google API Request
     *
     * @param searchText the search string
     * @param searchCriteria the search criteria
     * @param startIndex
     * @param maxResults
     * @return the url
     */
    private String buildGoogleRequestUrl(String searchText, String searchCriteria, int startIndex, int maxResults) {
        searchCriteriaToGoogleQueryCriteria.put("author", "inauthor");
        searchCriteriaToGoogleQueryCriteria.put("title", "intitle");
        String searchTerm = searchCriteriaToGoogleQueryCriteria.get(searchCriteria);

        return String.format(ENDPOINT_GOOGLE_SEARCH, startIndex, maxResults, LANGUAGE_RESTRICTION, searchTerm, searchText, googleBooksApiKey);
    }

    /** Send Request to Google Books API
     *
     * @param requestUrl the request url
     * @return List of found books
     */
    private List<Book> sendRequest(String requestUrl) {
        System.out.println("GETTING RESULTS FROM GOOGLE  =======================================");

        GoogleBooksApiResponse googleBooksApiResponse;
        try {
            googleBooksApiResponse = restTemplate.getForObject(requestUrl, GoogleBooksApiResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        List<Book> books = new ArrayList<>();
        if (googleBooksApiResponse != null && googleBooksApiResponse.getTotalItems() > 0) {
            List<Item> foundItems = googleBooksApiResponse.getItems();
            for (Item item : foundItems) {
                VolumeInfo volumeInfo = item.getVolumeInfo();
                if (volumeInfo == null) {
                    continue;
                }
                Book book = parseVolumeInfoToBook(volumeInfo);
                books.add(book);
            }
            return books;
        }

        System.out.println("RETURNING NULL  =======================================");
        return null;
    }
}
