package codingnomads.bibliotrackbooklibrary.controller;

import codingnomads.bibliotrackbooklibrary.BibliotrackBookLibraryApplication;
import codingnomads.bibliotrackbooklibrary.exception.UserExceptions;
import codingnomads.bibliotrackbooklibrary.model.security.UserPrincipal;
import codingnomads.bibliotrackbooklibrary.configuration.SecurityConfig;
import codingnomads.bibliotrackbooklibrary.service.UserPrincipalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {BibliotrackBookLibraryApplication.class, SecurityConfig.class}
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.cache.type=none" // Deactivates chaching for tests
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPrincipalService customUserDetailsServiceMock;

    @InjectMocks
    private UserController userControllerMock;

    private UserPrincipal newUserPrincipal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userControllerMock).build();

        newUserPrincipal = new UserPrincipal();
        newUserPrincipal.setUsername("testUser");
        newUserPrincipal.setPassword("testPassword");
    }

    @Test
    public void registerNewUser_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "testUser")
                        .param("password", "testPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.flash().attribute("registerSuccessMessage", "User registered successfully"));
    }

    @Test
    public void registerNewUser_UsernameAlreadyExists() throws Exception {
        String testUsername = "testUser";
        ArgumentCaptor<UserPrincipal> captor = ArgumentCaptor.forClass(UserPrincipal.class);

        doThrow(new UserExceptions.UsernameAlreadyExistsException(newUserPrincipal.getUsername()))
                .when(customUserDetailsServiceMock).createNewUserPrincipal(captor.capture());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", testUsername)
                        .param("password", "testPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andExpect(MockMvcResultMatchers.flash().attribute("usernameErrorMessage",
                        "The username '" + testUsername + "' already exists. " +
                                "Please choose a different username."));
    }

    @Test
    public void registerNewUser_InvalidUsername() throws Exception {
        String testUsername = "testUser";
        ArgumentCaptor<UserPrincipal> captor = ArgumentCaptor.forClass(UserPrincipal.class);

        doThrow(new UserExceptions.InvalidUsernameException("Invalid username"))
                .when(customUserDetailsServiceMock).createNewUserPrincipal(captor.capture());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", testUsername)
                        .param("password", "testPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andExpect(MockMvcResultMatchers.flash().attribute("usernameErrorMessage", "Invalid username"));
    }

    @Test
    public void registerNewUser_InvalidPassword() throws Exception {
        String testPassword = "invalidPassword";
        ArgumentCaptor<UserPrincipal> captor = ArgumentCaptor.forClass(UserPrincipal.class);

        doThrow(new UserExceptions.InvalidPasswordException("Invalid password"))
                .when(customUserDetailsServiceMock).createNewUserPrincipal(captor.capture());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "testUser")
                        .param("password", testPassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andExpect(MockMvcResultMatchers.flash().attribute("passwordErrorMessage", "Invalid password"));
    }

    @Test
    void deleteUser_Success() throws Exception {
        Long userId = 1L;
        doNothing().when(customUserDetailsServiceMock).deleteUserById(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/delete_user/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));
    }

    @Test
    public void deleteUser_ExceptionThrown() throws Exception {
        Long userId = 1L;
        doThrow(new UserExceptions.OperationUnsuccessfulException("User not found")).when(customUserDetailsServiceMock).deleteUserById(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/delete_user/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("operationUnsuccessful", "Operation was not successful. Please try again."));
    }
}