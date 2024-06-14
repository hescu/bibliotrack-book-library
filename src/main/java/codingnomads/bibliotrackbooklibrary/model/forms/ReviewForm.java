package codingnomads.bibliotrackbooklibrary.model.forms;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewForm {

    private String username;
    private String isbn;
    private int rating;
    private String review;
}
