package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;

import java.util.List;

public interface IBookApi {
    List<Book> performSearch(SearchFormData searchFormData);

    Book searchBookByIsbn(String isbn);
}
