package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GoogleBooksApiResponse {
    private int totalItems;
    private List<Item> items;
}
