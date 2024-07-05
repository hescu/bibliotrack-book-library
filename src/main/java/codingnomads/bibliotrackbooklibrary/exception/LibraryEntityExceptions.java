package codingnomads.bibliotrackbooklibrary.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class LibraryEntityExceptions {

    @ResponseStatus
    public static class WishlistException extends RuntimeException {
        public WishlistException(String message) {
            super(message);
        }
    }


    @ResponseStatus
    public static class BookshelfException extends RuntimeException {
        public BookshelfException(String message) {
            super(message);
        }
    }
}
