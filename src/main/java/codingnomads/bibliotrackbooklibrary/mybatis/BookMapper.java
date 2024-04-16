package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BookMapper {

    @Select("SELECT * FROM book WHERE book.id = #{bookId};")
    Book getBookById(Long bookId);
}
