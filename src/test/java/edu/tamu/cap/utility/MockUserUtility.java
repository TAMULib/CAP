package edu.tamu.cap.utility;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.User;
import edu.tamu.weaver.auth.model.Credentials;

@Service
public class MockUserUtility {

    @Value("classpath:mock/credentials/aggiejack.json")
    private Resource aggiejack;

    @Value("classpath:mock/credentials/aggiejane.json")
    private Resource aggiejane;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Credentials getMockAggieJackCredentials() throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(aggiejack.getFile(), Credentials.class);
    }

    public Credentials getMockAggieJaneCredentials() throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(aggiejane.getFile(), Credentials.class);
    }

    public User getMockAggieJackUser() throws JsonParseException, JsonMappingException, IOException {
      Credentials credentials = getMockAggieJackCredentials();
      return new User(credentials.getEmail(), credentials.getFirstName(), credentials.getLastName(), credentials.getRole());
    }

    public User getMockAggieJaneUser() throws JsonParseException, JsonMappingException, IOException {
      Credentials credentials = getMockAggieJaneCredentials();
      return new User(credentials.getEmail(), credentials.getFirstName(), credentials.getLastName(), credentials.getRole());
    }

}
