package codingnomads.bibliotrackbooklibrary.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class LibraryEntityExceptions {

    @ResponseStatus
    public static class WishlistNotFound extends RuntimeException {
        public WishlistNotFound(String message) {
            super(message);
        }
    }

    @ResponseStatus
    public static class BookshelfNotFoundException extends RuntimeException {
        public BookshelfNotFoundException(String message) {
            super(message);
        }
    }
}
