package codingnomads.bibliotrackbooklibrary.entity.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Item {
    private String kind;
    private String id;
    private VolumeInfo volumeInfo;
}
