package codingnomads.bibliotrackbooklibrary.entity.thymeleaf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String isbn;
    private String title;
    private List<String> authors;
    private String thumbnail;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
}
