package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView addBookToWishlist(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
        try {
            libraryService.addBookToWishlist(isbn);
            redirectAttributes.addFlashAttribute("message", "Book added to wishlist successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return new ModelAndView("redirect:/search");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to add book to wishlist: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return new ModelAndView("redirect:/error");
        }
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
