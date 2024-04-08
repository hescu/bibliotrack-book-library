package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String title;
    private List<String> authors;
    private String thumbnail;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;

    @ManyToMany(mappedBy = "books")
    private List<Wishlist> wishlists;
}
