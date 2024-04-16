package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT * " +
            "FROM user " +
            "WHERE username = #{param1};")
    Optional<User> findByUsername(String username);

    @Select("SELECT u.id, w.id as wishlist_id, b.id as book_id, b.isbn, b.title, b.authors, b.thumbnail, b.publisher, b.published_date, b.description, b.page_count " +
            "FROM user u " +
            "JOIN wishlist w ON u.wishlist_id = w.id " +
            "LEFT JOIN wishlist_book wb ON w.id = wb.wishlist_id " +
            "LEFT JOIN book b ON wb.book_id = b.id " +
            "WHERE u.id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "wishlist.id", column = "wishlist_id"),
            @Result(property = "wishlist.books", javaType = List.class, column = "book_id",
                    many = @Many(select = "BookMapper.getBookById"))
    })
    User getUserWithWishlistAndBooks(Long userId);
}
