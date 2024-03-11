package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;
}
