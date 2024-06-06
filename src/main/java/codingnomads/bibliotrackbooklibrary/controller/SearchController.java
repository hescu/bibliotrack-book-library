package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.model.forms.AddToBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @Autowired
    LibraryService libraryService;

    /**
     * Display search page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping()
    public String displaySearchPage(Model model) {
        model.addAttribute("searchResults", new ArrayList<>());
        model.addAttribute("searchFormData", new SearchFormData());
        model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
        model.addAttribute("addToBookshelfFormData", new AddToBookshelfFormData());
        return "search";
    }

    /**
     * Perform search string.
     *
     * @param searchFormData the search form data
     * @param model          the model
     * @return the string
     */
    @PostMapping()
    public String performSearch(@ModelAttribute("searchFormData") SearchFormData searchFormData,
                                Model model) {
        System.out.println("SEARCH CONTROLLER  =======================================");
        long start = System.nanoTime();
        model.addAttribute("searchResults", searchService.performSearch(searchFormData));
        model.addAttribute("addToBookshelfFormData", new AddToBookshelfFormData());
        model.addAttribute("searchFormData", new SearchFormData());
        model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
        long end = System.nanoTime();
        long timeElapsed = end - start;
        System.out.println("SearchTime: " + timeElapsed);
        return "search";
    }
}
