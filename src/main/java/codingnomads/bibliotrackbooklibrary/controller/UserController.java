package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.exception.UserExceptions;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.service.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class UserController {

    @Autowired
    UserPrincipalService customUserDetailsService;

    @PostMapping(path = "/register")
    public String registerNewUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            UserPrincipal newUserPrincipal = new UserPrincipal();
            newUserPrincipal.setUsername(username);
            newUserPrincipal.setPassword(password);
            customUserDetailsService.createNewUser(newUserPrincipal);
            redirectAttributes.addFlashAttribute("registerSuccessMessage", "User registered successfully");
            return "redirect:/login";
        } catch (UserExceptions.UsernameAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("usernameErrorMessage", e.getMessage());
            return "redirect:/register";
        } catch (UserExceptions.InvalidPasswordException e) {
            redirectAttributes.addFlashAttribute("passwordErrorMessage", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            return "errors/error";
        }
    }

    @PostMapping(path = "/admin/delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            customUserDetailsService.deleteUserById(id);
            return "redirect:/admin";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "redirect:/errors/error";
        }
    }
}
