package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.model.response.Item;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    SearchService searchService;

    @GetMapping
    public String displayIndex() {
        return "index";
    }

    @GetMapping("/search")
    public String displaySearchPage() {
        return "search";
    }

    @PostMapping(value = "/search")
    public String performSearch(@RequestParam String searchText, Model model) {
        List<Item> searchResults = searchService.performSearch(searchText);
        model.addAttribute("searchResults", searchResults);
        return "index";
    }
}
