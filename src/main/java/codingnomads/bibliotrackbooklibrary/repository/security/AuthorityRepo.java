package codingnomads.bibliotrackbooklibrary.repository.security;

import codingnomads.bibliotrackbooklibrary.model.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {
}
