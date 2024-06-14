package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.forms.AddToBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (searchFormData == null) {
            searchFormData = new SearchFormData();
        }

        // Perform search and handle possible null return
        List<Book> searchResults = searchService.performSearch(searchFormData);
        if (searchResults == null) {
            searchResults = Collections.emptyList();
        }
        model.addAttribute("searchResults", searchResults);

        // Check if addToBookshelfFormData and searchFormData need to be added to the model
        AddToBookshelfFormData addToBookshelfFormData = new AddToBookshelfFormData();
        model.addAttribute("addToBookshelfFormData", addToBookshelfFormData);

        model.addAttribute("searchFormData", searchFormData);

        // Fetch bookshelves and handle possible null return
        List<Bookshelf> bookshelfList = libraryService.fetchBookshelves();
        if (bookshelfList == null) {
            bookshelfList = Collections.emptyList();
        }
        model.addAttribute("bookshelfList", bookshelfList);

        return "search";
    }
}
