package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.model.AddToBookshelfFormData;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import codingnomads.bibliotrackbooklibrary.model.RemoveFromBookshelfFormData;
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


    /**
     * Display my-library template.
     *
     * @param model model
     * @return string
     */
    @GetMapping()
    public String displayMyLibrary(Model model) {
        System.out.println(" DISPLAYING LIBRARY ");
        model.addAttribute("wishlist", libraryService.fetchBooksFromWishlist());
        model.addAttribute("bookshelfList", libraryService.fetchBookshelves());
        model.addAttribute("removeBookFromBookshelfFormData", new RemoveFromBookshelfFormData());
        System.out.println("FOUND WISHLISTS AND BOOKSHELVES");

        return "my-library";
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
        return "my-library";
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
            System.out.println("ISBN IN CONTROLLER: " + addToBookshelfPostRequest.getFormDataISBN());
            System.out.println("BOOKSHELF ID IN CONTROLLER: " + addToBookshelfPostRequest.getBookshelfId());
            libraryService.addBookToBookshelf(addToBookshelfPostRequest.getFormDataISBN(), addToBookshelfPostRequest.getBookshelfId());
            redirectAttributes.addFlashAttribute("message", "Book added to bookshelf successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return new ModelAndView("redirect:/search");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to add book to bookshelf: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return new ModelAndView("redirect:/error");
        }
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
}
