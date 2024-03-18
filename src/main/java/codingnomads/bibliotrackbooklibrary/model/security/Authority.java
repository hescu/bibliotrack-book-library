package codingnomads.bibliotrackbooklibrary.model.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
