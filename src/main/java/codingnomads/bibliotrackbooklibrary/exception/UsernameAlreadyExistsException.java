package codingnomads.bibliotrackbooklibrary.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

     public UsernameAlreadyExistsException(String username) {
         super("The username '" + username + "' already exists. Please choose a different username.");
     }
}
