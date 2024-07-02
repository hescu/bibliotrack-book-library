package codingnomads.bibliotrackbooklibrary.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "errors/error";

    /**
     * Default error handler model and view.
     *
     * @param req the req
     * @param e   the e
     * @return the model and view
     * @throws Exception the exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(SearchExceptions.SearchResultsNotFoundException.class)
    public String handleSearchResultsNotFoundException(SearchExceptions.SearchResultsNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "search";
    }

    @ExceptionHandler(LibraryEntityExceptions.BookshelfNotFoundException.class)
    public String handleBookshelfNotFoundException(LibraryEntityExceptions.BookshelfNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "search";
    }
}
