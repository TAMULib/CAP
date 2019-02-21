// package edu.tamu.cap.auth.service;


// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// import java.io.IOException;
// import java.util.Collection;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.mockito.junit.jupiter.MockitoSettings;
// import org.mockito.quality.Strictness;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;

// import edu.tamu.cap.auth.AuthMockTests;
// import edu.tamu.cap.model.User;
// import edu.tamu.weaver.auth.model.Credentials;

// @ExtendWith(MockitoExtension.class)
// @ExtendWith(SpringExtension.class)
// @MockitoSettings(strictness = Strictness.WARN)
// public final class AppUserDetailsServiceTest extends AuthMockTests {

//     @InjectMocks
//     private AppUserDetailsService appUserDetailsService;

//     @Test
//     public void testBuildUserDetails() throws JsonParseException, JsonMappingException, IOException {
//         Credentials credentials = getMockAggieJackCredentials();
//         User user = new User(credentials.getUin(), credentials.getFirstName(), credentials.getLastName(), credentials.getRole());
//         UserDetails userDetails = appUserDetailsService.buildUserDetails(user);
//         assertNotNull(userDetails);
//         assertEquals(credentials.getUin(), userDetails.getUsername(), "User details had the incorrect username!");
//         Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//         assertNotNull(authorities);
//         assertEquals(1, authorities.size(), "User details had the incorrect number of authorities!");
//         assertEquals(credentials.getRole(), authorities.toArray(new GrantedAuthority[authorities.size()])[0].getAuthority(), "User details had the incorrect authority!");
//     }
// }
