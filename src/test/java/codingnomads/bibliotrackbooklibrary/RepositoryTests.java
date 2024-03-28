package codingnomads.bibliotrackbooklibrary;

import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.repository.security.UserPrincipalRepo;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Profile("test")
public class RepositoryTests {

    @Mock
    UserPrincipalRepo userPrincipalRepo;

    @Test
    public void checkIfUsernameAlreadyExists() {
        String username = "testUsername";
        UserPrincipal userPrincipal = new UserPrincipal();
        when(userPrincipalRepo.findByUsername(username)).thenReturn(Optional.of(userPrincipal));

        Optional<UserPrincipal> userPrincipalFound = userPrincipalRepo.findByUsername(username);

        assertTrue(userPrincipalFound.isPresent());
        verify(userPrincipalRepo, times(1)).findByUsername(username);
    }

    @Test
    void testExistsByUsername_NotFound() {
        String username = "nonexistentUsername";
        UserPrincipal userPrincipal = new UserPrincipal();
        when(userPrincipalRepo.findByUsername(username)).thenReturn(Optional.of(userPrincipal));

        Optional<UserPrincipal> userPrincipalFound = userPrincipalRepo.findByUsername(username);

        assertFalse(userPrincipalFound.isEmpty());
        verify(userPrincipalRepo, times(1)).findByUsername(username);
    }
}
