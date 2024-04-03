package codingnomads.bibliotrackbooklibrary.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserPrincipalMapper {

    @Select("SELECT COUNT(username) " +
            "FROM bibliotrack.user_principal " +
            "WHERE username = #{param1};")
    int countUsernames(String username);
}
