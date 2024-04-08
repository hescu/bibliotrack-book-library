package codingnomads.bibliotrackbooklibrary.repository;

import codingnomads.bibliotrackbooklibrary.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
}
