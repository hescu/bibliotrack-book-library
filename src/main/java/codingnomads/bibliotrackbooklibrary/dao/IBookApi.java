package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;

public interface IBookApi {
    GoogleBooksApiResponse performSearch(SearchFormData searchFormData);
}
