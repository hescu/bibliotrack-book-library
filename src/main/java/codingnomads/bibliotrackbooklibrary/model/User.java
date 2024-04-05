package codingnomads.bibliotrackbooklibrary.model;

import codingnomads.bibliotrackbooklibrary.entity.thymeleaf.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Wishlist wishlist;
}
