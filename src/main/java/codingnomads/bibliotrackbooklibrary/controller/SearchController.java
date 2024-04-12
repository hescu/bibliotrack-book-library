package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.entity.response.Item;
import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.model.SearchFormData;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping()
    public String displaySearchPage(Model model) {
        List<ThymeleafBook> emptyList = new ArrayList<>();
        model.addAttribute("searchResults", emptyList);
        model.addAttribute("searchFormData", new SearchFormData());
        return "search";
    }

    @PostMapping()
    public String performSearch(@ModelAttribute("searchFormData") SearchFormData searchFormData,
                                Model model) {
        model.addAttribute("searchResults", searchService.performSearch(searchFormData));
        return "search";
    }
}
