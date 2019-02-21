// package edu.tamu.cap.auth;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.Resource;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import edu.tamu.weaver.auth.model.Credentials;

// public abstract class AuthMockTests {
//     @Value("classpath:mock/credentials/aggiejack.json")
//     private Resource aggiejack;

//     @Value("classpath:mock/credentials/aggiejane.json")
//     private Resource aggiejane;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     protected synchronized Credentials getMockAggieJackCredentials() throws JsonParseException, JsonMappingException, IOException {
//         return objectMapper.readValue(aggiejack.getFile(), Credentials.class);
//     }

//     protected synchronized Credentials getMockAggieJaneCredentials() throws JsonParseException, JsonMappingException, IOException {
//         return objectMapper.readValue(aggiejane.getFile(), Credentials.class);
//     }
// }
