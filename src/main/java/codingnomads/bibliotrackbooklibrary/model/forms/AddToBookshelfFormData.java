package codingnomads.bibliotrackbooklibrary.model.forms;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToBookshelfFormData {

    private Long bookshelfId;
    private String formDataISBN;

}
