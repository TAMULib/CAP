package edu.tamu.cap.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.cap.model.Role;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public final class AppUserCredentialsServiceTest {

    @Autowired
    private MockUserUtility mockUserUtility;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AppUserCredentialsService credentialsService;

    private Credentials aggiejackCredentials;

    private Credentials aggiejackCredentialsWithoutRole;

    private Credentials aggiejackCredentialsUpdated;

    private User aggiejackUser;

    @BeforeEach
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);

        setField(credentialsService, "admins", new String[] { "123456789", "987654321" });

        aggiejackCredentials = mockUserUtility.getMockAggieJackCredentials();

        aggiejackCredentialsWithoutRole = mockUserUtility.getMockAggieJackCredentials();
        aggiejackCredentialsWithoutRole.setRole(null);

        aggiejackCredentialsUpdated = mockUserUtility.getMockAggieJackCredentials();
        aggiejackCredentialsUpdated.setRole("ROLE_CURATOR");
        aggiejackCredentialsUpdated.setEmail("jaggie@tamu.edu");
        aggiejackCredentialsUpdated.setFirstName("John");
        aggiejackCredentialsUpdated.setLastName("Agriculture");
        aggiejackCredentialsUpdated.setUin("123456781");

        aggiejackUser = new User(aggiejackCredentials.getEmail(), aggiejackCredentials.getFirstName(), aggiejackCredentials.getLastName(), aggiejackCredentials.getRole());
    }

    @Test
    public void testUpdateUserByCredentials() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(aggiejackUser));
        when(userRepo.create(any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn(aggiejackUser);
        when(userRepo.save(any(User.class))).thenReturn(new User(aggiejackCredentialsUpdated.getEmail(), aggiejackCredentialsUpdated.getFirstName(), aggiejackCredentialsUpdated.getLastName(), aggiejackCredentialsUpdated.getRole()));
        User user = credentialsService.updateUserByCredentials(aggiejackCredentialsUpdated);

        assertEquals(aggiejackUser, user, "Unable to update user with credentials!");
    }

    @Test
    public void testGetAnonymousRole() {
        assertEquals(Role.ROLE_ANONYMOUS.toString(), credentialsService.getAnonymousRole(), "Incorrect anonymous role returned from credentials service!");
    }

    @Test
    public void testUpdateUserByCredentialsWithoutRole() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(aggiejackUser));
        when(userRepo.save(any(User.class))).thenReturn(aggiejackUser);
        User userWithDefaultRole = credentialsService.updateUserByCredentials(aggiejackCredentialsWithoutRole);

        assertEquals(Role.ROLE_USER, userWithDefaultRole.getRole(), "User had incorrect default role!");
    }

    @Test
    public void testChangedUser() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(aggiejackUser));
        when(userRepo.save(any(User.class))).thenReturn(new User(aggiejackCredentialsUpdated.getEmail(), aggiejackCredentialsUpdated.getFirstName(), aggiejackCredentialsUpdated.getLastName(), aggiejackCredentialsUpdated.getRole()));
        User userUpdate = credentialsService.updateUserByCredentials(aggiejackCredentialsUpdated);

        assertEquals(aggiejackCredentialsUpdated.getLastName(), userUpdate.getLastName(), "User had the incorrect last name!");
        assertEquals(aggiejackCredentialsUpdated.getFirstName(), userUpdate.getFirstName(), "User had the incorrect first name!");
        assertEquals(aggiejackCredentialsUpdated.getUin(), userUpdate.getUsername(), "User had the incorrect username!");
        assertEquals(Role.valueOf(aggiejackCredentialsUpdated.getRole()), userUpdate.getRole(), "User had the incorrect role!");
    }

}
