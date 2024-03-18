package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.entity.google.response.GoogleBooksApiResponse;
import codingnomads.bibliotrackbooklibrary.entity.google.response.Item;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @Autowired
    GoogleBookApi googleBookApi;

    @GetMapping()
    public String displaySearchPage(Model model) {
        List<Item> emptyList = new ArrayList<>();
        model.addAttribute("searchResults", emptyList);
        return "search";
    }

    @PostMapping()
    public String performSearch(@RequestParam String searchText,
                                @RequestParam String searchCriteria,
                                @RequestParam(defaultValue = "1") int page,
                                Model model) {
        GoogleBooksApiResponse googleBooksApiResponse = searchService.performSearch(searchText, searchCriteria, page);
        int totalItemsFound = googleBooksApiResponse.getTotalItems();
        List<Item> searchResults = googleBooksApiResponse.getItems();
        model.addAttribute("totalItemsFound", totalItemsFound);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchService.calculateTotalPages(
                googleBooksApiResponse.getTotalItems(),
                googleBookApi.getITEMS_PER_PAGE())
        );
        return "search";
    }
}
