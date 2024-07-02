package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.cache.CacheService;
import codingnomads.bibliotrackbooklibrary.dao.SearchFormDataHolder;
import codingnomads.bibliotrackbooklibrary.exception.LibraryEntityExceptions;
import codingnomads.bibliotrackbooklibrary.exception.SearchExceptions;
import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.forms.AddToBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import codingnomads.bibliotrackbooklibrary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    private SearchFormDataHolder searchFormDataHolder;

    @Autowired
    private CacheService cacheService;

    /**
     * Display search page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping()
    public String displaySearchPage(Model model) {
        if (!model.containsAttribute("searchResults")) {
            model.addAttribute("searchResults", new ArrayList<>());
        }
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
        List<Book> searchResults = new ArrayList<>();
        List<Bookshelf> bookshelfList = new ArrayList<>();
        AddToBookshelfFormData addToBookshelfFormData = new AddToBookshelfFormData();

        if (searchFormData == null) {
            searchFormData = new SearchFormData();
        }

        // Perform search and handle possible null return
        try {
            searchFormDataHolder.setSearchFormData(searchFormData);
            searchResults = searchService.performSearch(searchFormData);
            if (searchResults == null) {
                searchResults = new ArrayList<>();
                throw new SearchExceptions.SearchResultsNotFoundException("The search came up empty. Please modify your search.");
            }

            // Fetch bookshelves and handle possible null return
            bookshelfList = libraryService.fetchBookshelves();
            if (bookshelfList == null) {
                bookshelfList = new ArrayList<>();
                throw new LibraryEntityExceptions.BookshelfNotFoundException("There was a problem with retrieving your bookshelves.");
            }
        } catch (SearchExceptions.SearchResultsNotFoundException | LibraryEntityExceptions.BookshelfNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("bookshelfList", bookshelfList);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("addToBookshelfFormData", addToBookshelfFormData);
        model.addAttribute("searchFormData", searchFormData);

        return "search";
    }

    /**
     * Add book to wishlist.
     *
     * @param isbn               the isbn
     * @param redirectAttributes the redirect attributes
     * @return the model and view
     */
    @Loggable
    @PostMapping("/wishlist/add")
    public ModelAndView addBookToWishlist(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        List<Book> cachedSearchResults = cacheService.getCachedSearchResults(searchFormDataHolder.getSearchFormData());
        try {
            libraryService.addBookToWishlist(isbn);

            redirectAttributes.addFlashAttribute("searchResults", cachedSearchResults);
            redirectAttributes.addFlashAttribute("message", "Book added to wishlist successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");

            modelAndView.setViewName("redirect:/search");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("searchResults", cachedSearchResults);
            redirectAttributes.addFlashAttribute("message", "Failed to add book to wishlist: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

            modelAndView.setViewName("redirect:/search");
        }
        return modelAndView;
    }


    /**
     * Add book to bookshelf.
     *
     * @param addToBookshelfPostRequest the add to bookshelf post request
     * @param redirectAttributes        the redirect attributes
     * @return the model and view
     */
    @PostMapping("/bookshelf/add")
    public ModelAndView addBookToBookshelf(@ModelAttribute("addToBookshelfFormData") AddToBookshelfFormData addToBookshelfPostRequest, RedirectAttributes redirectAttributes) {
        try {
            libraryService.addBookToBookshelf(addToBookshelfPostRequest.getFormDataISBN(), addToBookshelfPostRequest.getBookshelfId());

            redirectAttributes.addFlashAttribute("message", "Book added to bookshelf successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");

            return new ModelAndView("redirect:/search");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to add book to bookshelf: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

            return new ModelAndView("redirect:/search");
        }
    }
}
