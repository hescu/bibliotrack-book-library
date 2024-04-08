package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import codingnomads.bibliotrackbooklibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/my-library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping("/wishlist/add")
    @ResponseBody
    public ResponseEntity<String> addBookToWishlist(
            @ModelAttribute("item") ThymeleafBook book,
            @AuthenticationPrincipal UserDetails userDetails) {
        libraryService.addBookToWishlist(book, userDetails.getUsername());
    }
}
