package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.google.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;

import java.util.List;

public interface IBookApi {
    GoogleBooksApiResponse performSearch(String searchTerm, String searchCriteria, int page);
}
