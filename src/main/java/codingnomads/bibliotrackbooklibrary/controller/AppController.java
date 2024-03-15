package codingnomads.bibliotrackbooklibrary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping("/")
    public String displayIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String displayLoginPage() { return "login"; }

    @PostMapping("/logout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String displayRegisterPage() {
        return "register";
    }
}
