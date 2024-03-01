package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VolumeInfo {

    private String kind;
    private String id;
    private List<IndustryIdentifier> industryIdentifiers;
    private String title;
    private List<String> author;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
}
