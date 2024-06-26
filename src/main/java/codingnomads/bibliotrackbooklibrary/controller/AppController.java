package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.logging.Loggable;
import codingnomads.bibliotrackbooklibrary.service.UserPrincipalService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserPrincipalService userPrincipalService;

    @GetMapping("/")
    public String displayIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String displayLoginPage() { return "login"; }

    /** Logout user
     *
     * @return index template
     */
    @PostMapping("/logout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:index";
    }

    @GetMapping("/register")
    public String displayRegisterPage() {
        return "register";
    }

    /**
     * Display admin page string.
     *
     * @param model       the model
     * @param userDetails the user details
     * @return admin template
     */
    @GetMapping("/admin")
    public String displayAdminPage(Model model,  @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("users", userPrincipalService.findAllUsers());
        model.addAttribute("loggedInUsername", userDetails.getUsername());
        return "admin";
    }
}
