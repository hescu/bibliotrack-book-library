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

    @ResponseStatus
    public static class InvalidUsernameException extends RuntimeException {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    @ResponseStatus
    public static class OperationUnsuccessfulException extends RuntimeException {
        public OperationUnsuccessfulException(String message) {
            super("Operation was not successful. Please try again.");
        }
    }
}
