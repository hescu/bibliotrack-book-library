package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    private Long id;
    private Wishlist wishlist;

    public User() {
        wishlist = new Wishlist();
    }
}
