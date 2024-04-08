package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;

import java.util.List;

public interface IBookApi {
    List<ThymeleafBook> performSearch(SearchFormData searchFormData);
}
