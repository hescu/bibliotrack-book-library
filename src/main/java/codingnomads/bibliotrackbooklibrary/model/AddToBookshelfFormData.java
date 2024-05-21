package codingnomads.bibliotrackbooklibrary.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToBookshelfFormData {

    private Long bookshelfId;
    private String isbn;

}
