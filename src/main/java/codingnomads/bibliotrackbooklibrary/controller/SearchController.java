package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.exception.LibraryEntityExceptions;
import codingnomads.bibliotrackbooklibrary.exception.SearchExceptions;
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
        List<Book> searchResults;
        List<Bookshelf> bookshelfList;
        AddToBookshelfFormData addToBookshelfFormData = new AddToBookshelfFormData();

        if (searchFormData == null) {
            searchFormData = new SearchFormData();
        }

        // Perform search and handle possible null return
        searchResults = searchService.performSearch(searchFormData);
        if (searchResults == null) {
            throw new SearchExceptions.SearchResultsNotFoundException("The search came up empty.");
        }

        // Fetch bookshelves and handle possible null return
        bookshelfList = libraryService.fetchBookshelves();
        if (bookshelfList == null) {
            throw new LibraryEntityExceptions.BookshelfNotFoundException("The bookshelf couldn't be found.");
        }

        model.addAttribute("bookshelfList", bookshelfList);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("addToBookshelfFormData", addToBookshelfFormData);
        model.addAttribute("searchFormData", searchFormData);

        return "search";
    }
}
