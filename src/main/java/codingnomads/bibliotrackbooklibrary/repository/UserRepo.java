package codingnomads.bibliotrackbooklibrary.repository;

import codingnomads.bibliotrackbooklibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
