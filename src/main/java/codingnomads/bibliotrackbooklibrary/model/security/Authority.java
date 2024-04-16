package codingnomads.bibliotrackbooklibrary.model.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority implements GrantedAuthority {

    private Long id;

    private RoleEnum authority;

    public Authority(RoleEnum roleEnum) {
        this.authority = roleEnum;
    }

    public String getAuthority() {
        return authority.name();
    }

    public enum RoleEnum {
        ROLE_USER,
        ROLE_ADMIN
    }
}
