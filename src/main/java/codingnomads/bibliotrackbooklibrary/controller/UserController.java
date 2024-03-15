package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.service.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    UserPrincipalService customUserDetailsService;

    @PostMapping(path = "/register")
    public String registerNewUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        UserPrincipal newUserPrincipal = new UserPrincipal();
        newUserPrincipal.setUsername(username);
        newUserPrincipal.setPassword(password);
        try {
            customUserDetailsService.createNewUser(newUserPrincipal);
            redirectAttributes.addFlashAttribute("message", "User registered successfully");
            return "login";
        } catch (Exception e) {
            return "error";
        }
    }
}
