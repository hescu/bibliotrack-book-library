package codingnomads.bibliotrackbooklibrary.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustryIdentifier {

    private String type;
    private String identifier;
}
