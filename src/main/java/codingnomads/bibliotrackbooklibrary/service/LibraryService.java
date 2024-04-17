package codingnomads.bibliotrackbooklibrary.service;

import codingnomads.bibliotrackbooklibrary.dao.GoogleBookApi;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.User;
import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.mybatis.UserMapper;
import codingnomads.bibliotrackbooklibrary.repository.BookRepo;
import codingnomads.bibliotrackbooklibrary.repository.UserRepo;
import codingnomads.bibliotrackbooklibrary.repository.WishlistRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private GoogleBookApi googleBookApi;

    @Transactional
    public void addBookToWishlist(String isbn) {
        try {
//            Book bookFromCache = getBooksFromCache("searchResultsCache", "Dan Abnett|author", isbn);
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                Wishlist wishlist = currentUser.getWishlist();
                wishlist.getBooks().add(googleBookApi.searchBookByIsbn(isbn));
                userRepo.save(currentUser);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserPrincipal userPrincipal) {
                return userPrincipal.getUser();
            }
        }
        return null;
    }

    private Book getBooksFromCache(String cacheName, String key, String isbn) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null) {
                Object cachedObject = wrapper.get();
                if (cachedObject instanceof List<?> cachedList) {
                    if (!cachedList.isEmpty() && cachedList.getFirst() instanceof Book) {
                        List<Book> cachedBookList = (List<Book>) cachedList;
                        return findBookByIsbn(cachedBookList, isbn);
                    }
                }
            }
        }
        return null;
    }

    private Book findBookByIsbn(List<Book> bookList, String isbn) {
         Optional<Book> optionalBook = bookList.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
         return optionalBook.orElse(null);
    }

    public Set<Book> fetchWishlist() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.getWishlist().getBooks();
        }
        return null;
    }
}
