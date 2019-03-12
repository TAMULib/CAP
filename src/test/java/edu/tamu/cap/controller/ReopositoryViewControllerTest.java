package edu.tamu.cap.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.http.MediaType;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;
import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.weaver.auth.model.Credentials;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

public final class ReopositoryViewControllerTest {

  private static final ConstraintDescriptionsHelper describeRepositoryView = new ConstraintDescriptionsHelper(
      RepositoryView.class);

  private static final String REPOSITORY_VIEW_URI = "/repository-view";

  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

  @MockBean
  private RepositoryViewRepo resositoryViewRepo;

  @MockBean
  private UserRepo userRepo;

  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private MockUserUtility mockUserUtility;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  RestDocumentationResultHandler rdh;

  @Before
  public void setUp() throws IOException {

    RepositoryView mockRV = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
    mockRV.setId(1L);
    when(resositoryViewRepo.getOne(1L)).thenReturn(mockRV);
    when(resositoryViewRepo.findOne(1L)).thenReturn(mockRV);
    when(resositoryViewRepo.update(Mockito.any(RepositoryView.class))).thenReturn(mockRV);

    Credentials aggiejackCredentials = mockUserUtility.getMockAggieJackCredentials();
    User mockUser = new User(aggiejackCredentials.getEmail(), aggiejackCredentials.getFirstName(), aggiejackCredentials.getLastName(), aggiejackCredentials.getRole()); 
    when(userRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.of(mockUser));

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void createRepositoryView() throws Exception {

    RepositoryView mockRV = resositoryViewRepo.getOne(1L);



    MockHttpServletRequestBuilder mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.post(REPOSITORY_VIEW_URI);
    // mockHttpServletRequestBuilder = mockHttpServletRequestBuilder.content(objectMapper.writeValueAsString(mockRV));

    // String repViewJson = "{"
    // +  "\"id\" : 1,"
    // +  "\"type\" : \"FEDORA\","
    // +  "\"name\" : \"TEST_REPOSITORY_VIEW_NAME\","
    // +  "\"rootUri\" : \"http://test-repository-view.org\","
    // +  "\"username\" : \"user\","
    // +  "\"password\" : \"1234\","
    // +  "\"schemas\" : [ ],"
    // +  "\"curators\" : [ ],"
    // + "\"metadataPrefixes\": [ ]"
    // + "}";

    

    String repViewJson = "{\"name\":\"Labs Fedora\",\"rootUri\":\"https://api-dev.library.tamu.edu/fcrepo/rest/\",\"username\":\"fedoraAdmin\",\"password\":\"secret3\",\"type\":\"FEDORA\",\"schemas\":[],\"curators\":[]}";

    mockHttpServletRequestBuilder = mockHttpServletRequestBuilder.content(
      repViewJson
      );

    mockHttpServletRequestBuilder = mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON);


    System.out.println("\n\n\n");
    System.out.println(mockHttpServletRequestBuilder);
    System.out.println(objectMapper.writeValueAsString(mockRV));
    System.out.println(repViewJson.equals(objectMapper.writeValueAsString(mockRV)));
    System.out.println("\n\n\n");

    ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
      
    resultActions =  resultActions.andExpect(status().isOk())

    //   mockMvc
    //   .perform(RestDocumentationRequestBuilders.post(REPOSITORY_VIEW_URI).content(objectMapper.writeValueAsString(mockRV))
    //     .contentType(MediaType.APPLICATION_JSON))
    // .andExpect(status().isOk())
      // .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
      //   requestFields(
      //     describeRepositoryView.withField("id", "Repository View id.").ignored(),
      //     describeRepositoryView.withField("name", "The name of this Repository View."),
      //     describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
      //     describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
      //     describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
      //     describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
      //     describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
      //     describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
      //     describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
      //   )))
      ;

  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void allRepositoryViews() throws Exception {

    mockMvc
      .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void updateRepositoryView() throws Exception {

    RepositoryView mockRV = resositoryViewRepo.getOne(1L);
    mockRV.setName(TEST_REPOSITORY_VIEW_NAME+"_UPDATE");

    mockMvc
      .perform(RestDocumentationRequestBuilders.put(REPOSITORY_VIEW_URI)
          .content(objectMapper.writeValueAsString(mockRV))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deleteRepositoryView() throws Exception {

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getRepositoryView() throws Exception {

  }

}