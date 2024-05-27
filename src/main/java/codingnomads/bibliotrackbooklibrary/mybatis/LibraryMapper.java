package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.Book;
import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LibraryMapper {

    @Delete("DELETE FROM wishlist_book " +
            "WHERE wishlist_id = #{wishlistId} " +
            "AND book_id = #{bookId};")
    void removeBookFromWishlist(Long wishlistId, Long bookId);

    @Select("SELECT * FROM book " +
            "WHERE isbn = #{isbn};")
    Book findBookByIsbn(String isbn);

    @Select("SELECT id FROM bookshelf " +
            "WHERE user_id = #{userId};")
    List<Long> findAllBookshelvesIDsByUserId(Long userId);

    @Delete("DELETE FROM bookshelf_book " +
            "WHERE bookshelf_id = #{bookshelfId} " +
            "AND book_id = #{bookId};")
    void removeBookFromBookshelf(Long bookshelfId, Long bookId);

    /**
     * Add book to db.
     *
     * @param book the book
     */
    @Insert("INSERT INTO book (isbn, title, thumbnail, publisher, published_date, description, page_count) " +
            "VALUES (#{book.isbn}, #{book.title}, #{book.thumbnail}, #{book.publisher}, #{book.publishedDate}, #{book.description}, #{book.pageCount})")
    void addBookToDB(@Param("book") Book book);

    @Insert("INSERT INTO bookshelf_book (bookshelf_id, book_id)" +
            "VALUES (#{bookshelfId}, #{id})")
    void addBookToBookshelf(@Param("id") Long id, @Param("bookshelfId") Long bookshelfId);
}
