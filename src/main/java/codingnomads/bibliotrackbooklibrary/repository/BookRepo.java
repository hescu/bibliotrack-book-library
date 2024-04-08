package codingnomads.bibliotrackbooklibrary.repository;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.ThymeleafBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<ThymeleafBook, Long> {
}
