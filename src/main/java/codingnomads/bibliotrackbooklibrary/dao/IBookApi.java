package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;

import java.util.List;

public interface IBookApi {
    List<Item> preformSearch(String searchTerm, String searchCriteria);
}
