package codingnomads.bibliotrackbooklibrary.cache;

import codingnomads.bibliotrackbooklibrary.dao.SearchFormDataHolder;
import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public List<Book> getCachedSearchResults(SearchFormData searchFormData) {
        Cache cache = cacheManager.getCache("searchResultsCache");
        if (cache != null) {
            Object key = generateCacheKey(searchFormData);
            Cache.ValueWrapper cachedValue = cache.get(key);
            if (cachedValue != null) {
                return (List<Book>) cachedValue.get();
            }
        }
        return new ArrayList<>();
    }

    public void evictCache(SearchFormData searchFormData) {
        Cache cache = cacheManager.getCache("searchResultsCache");
        if (cache != null) {
            Object key = generateCacheKey(searchFormData);
            cache.evict(key);
        }
    }

    private Object generateCacheKey(SearchFormData searchFormData) {
        // Use the same key generation strategy as in @Cacheable
        return searchFormData.toString(); // Example key generation
    }
}

