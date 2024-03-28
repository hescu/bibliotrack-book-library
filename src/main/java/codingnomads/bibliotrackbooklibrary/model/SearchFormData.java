package codingnomads.bibliotrackbooklibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchFormData {

    private String searchString;
    private String searchCriteria;

    @JsonIgnore
    private int startIndex = 0;
    @JsonIgnore
    private int maxResults = 40;
}
