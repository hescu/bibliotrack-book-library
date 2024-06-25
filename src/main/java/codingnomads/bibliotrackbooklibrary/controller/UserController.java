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

@Controller
public class UserController {

    @Autowired
    UserPrincipalService customUserDetailsService;

    /**
     * Register new user string.
     *
     * @param username           the username
     * @param password           the password
     * @param redirectAttributes the redirect attributes
     * @return the string
     * @throws UserExceptions.UsernameAlreadyExistsException the exception
     */
    @PostMapping(path = "/register")
    public String registerNewUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) throws RuntimeException {
        try {
            UserPrincipal newUserPrincipal = new UserPrincipal();
            newUserPrincipal.setUsername(username);
            newUserPrincipal.setPassword(password);
            customUserDetailsService.createNewUserPrincipal(newUserPrincipal);
            redirectAttributes.addFlashAttribute("registerSuccessMessage", "User registered successfully");
            return "redirect:/login";
        } catch (UserExceptions.UsernameAlreadyExistsException | UserExceptions.InvalidUsernameException e) {
            redirectAttributes.addFlashAttribute("usernameErrorMessage", e.getMessage());
            return "redirect:/register";
        } catch (UserExceptions.InvalidPasswordException e) {
            redirectAttributes.addFlashAttribute("passwordErrorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Delete user string.
     *
     * @param id the id
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping(path = "/admin/delete_user/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customUserDetailsService.deleteUserById(id);
            return "redirect:/admin";
        } catch (UserExceptions.OperationUnsuccessfulException e) {
            redirectAttributes.addFlashAttribute("operationUnsuccessful", e.getMessage());
            return "redirect:/admin";
        }
    }
}
