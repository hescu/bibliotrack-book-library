package codingnomads.bibliotrackbooklibrary.mybatis;

import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserPrincipalMapper {

    @Select("SELECT COUNT(username) " +
            "FROM user_principal " +
            "WHERE username = #{param1};")
    int countUsernames(String username);

    @Select("SELECT * " +
            "FROM user_principal " +
            "WHERE username = #{param1};")
    Optional<UserPrincipal> findByUsername(String username);
}
