package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private String kind;
    private String id;
    private VolumeInfo volumeInfo;
}
