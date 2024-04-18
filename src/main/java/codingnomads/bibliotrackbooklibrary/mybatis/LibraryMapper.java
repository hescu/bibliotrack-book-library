package codingnomads.bibliotrackbooklibrary.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LibraryMapper {

    @Delete("DELETE FROM wishlist_book " +
            "WHERE wishlist_id = #{wishlistId} " +
            "AND book_id = #{bookId}")
    void removeBookFromWishlist(Long wishlistId, Long bookId);
}
