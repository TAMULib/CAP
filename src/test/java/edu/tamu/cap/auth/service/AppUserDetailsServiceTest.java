package edu.tamu.cap.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.cap.model.User;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public final class AppUserDetailsServiceTest {


    @Autowired  
    private MockUserUtility mockUserUtility;

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    public void testBuildUserDetails() throws JsonParseException, JsonMappingException, IOException {
        Credentials credentials = mockUserUtility.getMockAggieJackCredentials();
        User user = new User(credentials.getUin(), credentials.getFirstName(), credentials.getLastName(), credentials.getRole());
        UserDetails userDetails = appUserDetailsService.buildUserDetails(user);
        assertNotNull(userDetails);
        assertEquals(credentials.getUin(), userDetails.getUsername(), "User details had the incorrect username!");
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size(), "User details had the incorrect number of authorities!");
        assertEquals(credentials.getRole(), authorities.toArray(new GrantedAuthority[authorities.size()])[0].getAuthority(), "User details had the incorrect authority!");
    }
}
