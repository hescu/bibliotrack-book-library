package codingnomads.bibliotrackbooklibrary.exception;

public class UserExceptions {

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String username) {
            super("The username '" + username + "' already exists. Please choose a different username.");
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}
