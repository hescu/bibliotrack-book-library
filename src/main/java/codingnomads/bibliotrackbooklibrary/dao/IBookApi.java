package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.Book;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;

import java.util.List;

public interface IBookApi {
    List<Book> performSearch(SearchFormData searchFormData);
}
