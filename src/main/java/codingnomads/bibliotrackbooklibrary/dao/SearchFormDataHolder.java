package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.forms.SearchFormData;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFormDataHolder {

    private SearchFormData searchFormData;

}

