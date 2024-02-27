package codingnomads.bibliotrackbooklibrary.model;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Book {

    @GeneratedValue
    private Long id;
    private int isbn;
    private String title;
    private List<String> author;
    private String description;
}
