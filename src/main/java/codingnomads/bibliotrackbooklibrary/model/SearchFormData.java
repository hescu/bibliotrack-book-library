package codingnomads.bibliotrackbooklibrary.model;

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
}
