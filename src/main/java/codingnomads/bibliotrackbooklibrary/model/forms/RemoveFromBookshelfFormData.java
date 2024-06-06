package codingnomads.bibliotrackbooklibrary.model.forms;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveFromBookshelfFormData {
    private Long bookshelfId;
    private Long bookId;
}
