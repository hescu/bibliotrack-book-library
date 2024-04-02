package codingnomads.bibliotrackbooklibrary.repository.security;

import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipalRepo extends JpaRepository<UserPrincipal, Long> {

    @Query("SELECT COUNT(u) FROM UserPrincipal u WHERE u.username = :username")
    int countUsername(@Param("username") String username);

    Optional<UserPrincipal> findByUsername(String username);
}
