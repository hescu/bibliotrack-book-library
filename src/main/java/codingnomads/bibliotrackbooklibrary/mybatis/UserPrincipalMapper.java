package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserPrincipalMapper {

    @Select("SELECT COUNT(username) " +
            "FROM userprincipal " +
            "WHERE username = #{param1};")
    int countUsernames(String username);

    @Select("SELECT * " +
            "FROM userprincipal " +
            "WHERE username = #{param1};")
    Optional<UserPrincipal> findByUsername(String username);

    @Delete("DELETE FROM userprincipal_authority WHERE user_id = #{id};")
    int deleteFromUserAuthorityJoinTableById(Long id);

    @Delete("DELETE FROM userprincipal WHERE id = #{id}")
    int deleteUserPrincipalById(Long id);
}
