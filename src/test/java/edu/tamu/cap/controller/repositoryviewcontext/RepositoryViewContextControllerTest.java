package edu.tamu.cap.controller.repositoryviewcontext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.config.RestDocumentationConfiguration;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.RepositoryViewType;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes={RestDocumentationConfiguration.class})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

public class RepositoryViewContextControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RepositoryViewRepo resositoryViewRepo;

  // @Autowired
  // RestDocumentationResultHandler rdh;

  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST REPOSITORY VIEW";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";
  private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
  private static final String TEST_REPOSITORY_VIEW_CONTEXT_URI = "repository-view-context/{type}/{repositoryViewId}?contextUri="+TEST_REPOSITORY_VIEW_URI;

  private RepositoryView testRepositoryView;

  @Before
  public void setUp() throws JsonProcessingException {
    // RepositoryViewContext testRepositoryViewContext = new RepositoryViewContext();
    testRepositoryView = resositoryViewRepo.create(new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI));
  }

  @After
  public void tearDown() {
    resositoryViewRepo.deleteAll();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getRepositoryViewContext() throws Exception {

    mockMvc
      .perform(RestDocumentationRequestBuilders.get(TEST_REPOSITORY_VIEW_CONTEXT_URI, TEST_REPOSITORY_VIEW_TYPE, testRepositoryView.getId())
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
        pathParameters(parameterWithName("type").description("The type of the Repository view to be rendered as a Repository View Context.")),
        pathParameters(parameterWithName("repositoryViewId").description("The id of the Repository view to be rendered as a Repository View Context."))
      ));

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deleteRepositoryViewContext() throws Exception {

    // mockMvc
    //   .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI)
    //       .contentType(MediaType.APPLICATION_JSON))
    //   .andExpect(status().isOk())
    //   .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getTriples() throws Exception {

    // RepositoryView testRV = resositoryViewRepo.create(new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI));
    // ApiResponse expectedResponse = new ApiResponse(ApiStatus.SUCCESS, testRV);
    
    // mockMvc
    //   .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI+"/{id}", testRV.getId())
    //       .contentType(MediaType.APPLICATION_JSON))
    //   .andExpect(status().isOk())
    //   .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
    //   .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
    //     pathParameters(parameterWithName("id").description("Schema id"))));

  }

}