package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public ResponseEntity<String> addBookToWishlist(@RequestParam("isbn") String isbn) {
        try {
            libraryService.addBookToWishlist(isbn);
            return ResponseEntity.ok("Book added to wishlist successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book to wishlist." + e.getMessage());
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
