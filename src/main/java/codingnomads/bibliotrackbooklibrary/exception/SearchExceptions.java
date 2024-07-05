package codingnomads.bibliotrackbooklibrary.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class SearchExceptions {

    @ResponseStatus
    public static class SearchResultsNotFoundException extends RuntimeException {
        public SearchResultsNotFoundException(String message) {
            super(message);
        }
    }
}
