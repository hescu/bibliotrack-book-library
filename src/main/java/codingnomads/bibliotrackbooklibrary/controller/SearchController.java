package codingnomads.bibliotrackbooklibrary.controller;

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

    @GetMapping()
    public String displaySearchPage(Model model) {
        List<Item> emptyList = new ArrayList<>();
        model.addAttribute("searchResults", emptyList);
        return "search";
    }

    @PostMapping()
    public String performSearch(@RequestParam String searchText, @RequestParam String searchCriteria, Model model) {
        List<Item> searchResults = searchService.performSearch(searchText, searchCriteria);
        model.addAttribute("searchResults", searchResults);
        return "search";
    }
}
