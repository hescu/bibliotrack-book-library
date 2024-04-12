package codingnomads.bibliotrackbooklibrary.entity.thymeleaf;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ThymeleafBook {

    private String isbn;
    private String title;
    private List<String> authors;
    private String thumbnail;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
}
