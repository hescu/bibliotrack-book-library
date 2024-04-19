package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LibraryMapper {

    @Delete("DELETE FROM wishlist_book " +
            "WHERE wishlist_id = #{wishlistId} " +
            "AND book_id = #{bookId};")
    void removeBookFromWishlist(Long wishlistId, Long bookId);

    @Select("SELECT * FROM book " +
            "WHERE isbn = #{isbn};")
    Book findBookByIsbn(String isbn);
}
