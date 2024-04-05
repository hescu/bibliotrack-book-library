package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/my-library")
public class LibraryController {

    @Autowired


    @PostMapping("/wishlist/add")
    @ResponseBody
    public ResponseEntity<String> addBookToWishlist(@ModelAttribute("item") Book book) {

    }
}
