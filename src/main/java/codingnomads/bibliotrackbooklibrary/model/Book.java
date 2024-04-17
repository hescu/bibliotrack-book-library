package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private List<String> authors;
    private String thumbnail;
    private String publisher;
    private String publishedDate;

    @Lob
    @Column(length = 256)
    private String description;
    private int pageCount;

    @ManyToMany(mappedBy = "books")
    private List<Wishlist> wishlists;

    public Book(String isbn, String title, List<String> authors, String thumbnail, String publisher, String publishedDate, String description, int pageCount) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
    }
}
