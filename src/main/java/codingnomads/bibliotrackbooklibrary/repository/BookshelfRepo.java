package codingnomads.bibliotrackbooklibrary.repository;

import codingnomads.bibliotrackbooklibrary.model.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookshelfRepo extends JpaRepository<Bookshelf, Long> {
}
