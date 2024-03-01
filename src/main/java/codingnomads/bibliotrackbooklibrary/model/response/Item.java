package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private String kind;
    private String id;
    private VolumeInfo volumeInfo;
}
