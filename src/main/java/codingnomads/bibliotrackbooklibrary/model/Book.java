package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    private String thumbnail;
    private String publisher;
    private String publishedDate;

    @Lob
    @Column(length = 1024)
    private String description;
    private int pageCount;

    @ManyToMany(mappedBy = "books")
    private List<Wishlist> wishlists;

    public Book(String isbn, String title, List<Author> authors, String thumbnail, String publisher, String publishedDate, String description, int pageCount) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = sanitizeString(description);
        this.pageCount = pageCount;
    }

    private String sanitizeString(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void setDescription(String description) {
        this.description = sanitizeString(description);
    }
}
