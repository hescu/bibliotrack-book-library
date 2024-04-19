package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/my-library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping()
    public String displayMyLibrary(Model model) {
        model.addAttribute("wishlist", libraryService.fetchBooksFromWishlist());
        return "my-library";
    }

    @Loggable
    @PostMapping("/wishlist/add")
    public ModelAndView addBookToWishlist(@RequestParam("isbn") String isbn) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            libraryService.addBookToWishlist(isbn);
            modelAndView.setViewName("redirect:/search");
            modelAndView.addObject("message", "Book added to wishlist successfully.");
        } catch (Exception e) {
            modelAndView.setViewName("redirect:/error");
            modelAndView.addObject("message", "Failed to add book to wishlist: " + e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/wishlist/delete")
    public String removeBookFromWishlist(@RequestParam("bookId") Long bookId, Model model) {
        try {
            Wishlist wishlist = libraryService.removeBookFromWishlist(bookId);
            if (wishlist != null) {
                model.addAttribute("message", "Book removed from wishlist successfully.");
                model.addAttribute("wishlist", wishlist.getBooks());
            } else {
                model.addAttribute("error", "Failed to remove book from wishlist.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to remove book from wishlist: " + e.getMessage());
        }
        return "my-library";
    }
}
