package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id", nullable = false)
    private Wishlist wishlist;

    public User() {
        wishlist = new Wishlist();
    }
}
