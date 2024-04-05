package codingnomads.bibliotrackbooklibrary.repository;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
