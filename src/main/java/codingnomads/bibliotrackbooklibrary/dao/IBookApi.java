package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;

import java.util.List;

/**
 * The interface Book api.
 */
public interface IBookApi {

    /**
     * Perform search list.
     *
     * @param searchFormData the search form data
     * @return the list
     */
    List<Book> performSearch(SearchFormData searchFormData);


    /**
     * Default search list.
     *
     * @param searchString the search string
     * @return the list
     */
    List<Book> defaultSearch(String searchString);

    /**
     * Search book by isbn book.
     *
     * @param isbn the isbn
     * @return the book
     */
    Book searchBookByIsbn(String isbn);
}
