package codingnomads.bibliotrackbooklibrary.model;

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
