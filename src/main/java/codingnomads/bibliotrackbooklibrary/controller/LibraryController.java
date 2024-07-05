package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.AddToBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.forms.RemoveFromBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.forms.ReviewForm;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/my-library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;


    /**
     * Display my-library template.
     *
     * @param model model
     * @return string
     */
    @GetMapping()
    public String displayMyLibrary(Model model) {
        model.addAttribute("wishlist", libraryService.fetchBooksFromWishlist());
        model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
        model.addAttribute("removeBookFromBookshelfFormData", new RemoveFromBookshelfFormData());

        return "my-library";
    }


    /**
     * Remove book from wishlist.
     *
     * @param bookId the book id
     * @param model  the model
     * @return the string
     */
    @PostMapping("/wishlist/delete")
    public String removeBookFromWishlist(@RequestParam("bookId") Long bookId, Model model) {
        try {
            Wishlist wishlist = libraryService.removeBookFromWishlist(bookId);
            if (wishlist != null) {
                model.addAttribute("message", "Book removed from wishlist successfully.");
                model.addAttribute("wishlist", wishlist.getBooks());
                model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
            } else {
                model.addAttribute("error", "Failed to remove book from wishlist.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to remove book from wishlist: " + e.getMessage());
        }
        return "redirect:/my-library";
    }


    /**
     * Remove book from bookshelf.
     *
     * @param removeFromBookshelfFormData the remove from bookshelf form data
     * @param model                       the model
     * @return the string
     */
    @PostMapping("/bookshelf/delete")
    public String removeBookFromBookshelf(@ModelAttribute("removeBookFromBookshelfFormData") RemoveFromBookshelfFormData removeFromBookshelfFormData, Model model) {
        try {
            Bookshelf bookshelf = libraryService.removeBookFromBookshelf(
                    removeFromBookshelfFormData.getBookshelfId(),
                    removeFromBookshelfFormData.getBookId()
            );
            model.addAttribute("removeBookFromBookshelfFormData", new RemoveFromBookshelfFormData());
            model.addAttribute("wishlist", libraryService.fetchBooksFromWishlist());
            model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to remove book from bookshelf: " + e.getMessage());
        }
        return "my-library";
    }

    /**
     * Gets review form.
     *
     * @param isbn  the isbn
     * @param model the model
     * @return the review form
     */
    @GetMapping("/review-form/{isbn}")
    public String getReviewForm(@PathVariable("isbn") String isbn, Model model) {
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setIsbn(isbn);

        Book bookInfo = libraryService.getBookFromDatabase(isbn);

        model.addAttribute("reviewForm", reviewForm);
        model.addAttribute("bookInfo", bookInfo);

        return "review-form";
    }

    /**
     * Submit review string.
     *
     * @param reviewForm         the review form
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @PostMapping("/review-form/submit-review")
    public String submitReview(@ModelAttribute("reviewForm") ReviewForm reviewForm, Model model, RedirectAttributes redirectAttributes) {
        boolean isSuccessful = libraryService.postReview(reviewForm);
        if (isSuccessful) {
            redirectAttributes.addFlashAttribute("successMessage", "Review posted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to post review. Please try again.");
        }
        return "redirect:/my-library";
    }
}
