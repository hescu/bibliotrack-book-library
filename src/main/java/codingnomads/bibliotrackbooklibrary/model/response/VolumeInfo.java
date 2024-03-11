package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VolumeInfo {

    private String kind;
    private String id;
    private List<IndustryIdentifier> industryIdentifiers;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private ImageLinks imageLinks;
    private String language;
}
