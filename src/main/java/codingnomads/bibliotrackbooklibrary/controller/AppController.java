package codingnomads.bibliotrackbooklibrary.controller;

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
}
