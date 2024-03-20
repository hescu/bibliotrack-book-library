package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping("/")
    public String displayIndex() {
        return "index";
    }

    @Loggable
    @GetMapping("/login")
    public String displayLoginPage() { return "login"; }

    @Loggable
    @PostMapping("/logout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:index";
    }

    @Loggable
    @GetMapping("/register")
    public String displayRegisterPage() {
        return "register";
    }

    @GetMapping("/admin")
    public String displayAdminPage() { return "admin"; }
}
