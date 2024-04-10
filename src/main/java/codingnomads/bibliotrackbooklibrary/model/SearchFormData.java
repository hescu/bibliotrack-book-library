package codingnomads.bibliotrackbooklibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public String toString() {
        // Generate a unique string representation based on non-excluded properties
        return searchString + "|" + searchCriteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFormData that = (SearchFormData) o;
        return startIndex == that.startIndex &&
                maxResults == that.maxResults &&
                searchString.equals(that.searchString) &&
                searchCriteria.equals(that.searchCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchString, searchCriteria, startIndex, maxResults);
    }
}
