package codingnomads.bibliotrackbooklibrary.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class UserExceptions {

    @ResponseStatus
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String username) {
            super("The username '" + username + "' already exists. Please choose a different username.");
        }
    }

    @ResponseStatus
    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}
