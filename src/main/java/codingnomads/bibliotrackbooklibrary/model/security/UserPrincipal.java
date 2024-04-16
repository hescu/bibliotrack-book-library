package codingnomads.bibliotrackbooklibrary.model.security;

import codingnomads.bibliotrackbooklibrary.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private List<Authority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private User user;

    public UserPrincipal(String username, String password, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }
}
