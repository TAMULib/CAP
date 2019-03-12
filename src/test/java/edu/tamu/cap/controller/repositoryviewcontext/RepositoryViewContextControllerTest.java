package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.config.RestDocumentationConfiguration;
import edu.tamu.cap.controller.aspect.RepositoryViewInjectionAspect;
import edu.tamu.cap.exceptions.RepositoryViewInjectionException;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.ArgumentResolver;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;
import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { RestDocumentationConfiguration.class })
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

public class RepositoryViewContextControllerTest {

  private static final ConstraintDescriptionsHelper describeRepositoryViewContext = new ConstraintDescriptionsHelper(
      RepositoryViewContext.class);

  // @Autowired
  // private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  // @MockBean
  // private RepositoryViewRepo mockResositoryViewRepo;

  @MockBean
  private FedoraService mockFedoraService;

  private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
  private static final String TEST_REPOSITORY_VIEW_CONTEXT_URI = "/repository-view-context/{type}/{repositoryViewId}";
  private static final String MOCKCONTEXT_ORG_URI = "http://mockcontext.org";

  @Before
  public void setUp() throws Exception {

    RepositoryViewContext mockRepositoryViewContext = new RepositoryViewContext();

    ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

    Object[] args = new Object[] { mockFedoraService, MOCKCONTEXT_ORG_URI };

    when(mockFedoraService.getRepositoryViewContext(Mockito.any(String.class))).thenReturn(mockRepositoryViewContext);
    when(mockJoinPoint.getArgs()).thenReturn(args);

  }

  @After
  public void tearDown() {
    //resositoryViewRepo.deleteAll();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getRepositoryViewContext() throws Exception {
    mockMvc
      .perform(RestDocumentationRequestBuilders.get(TEST_REPOSITORY_VIEW_CONTEXT_URI, TEST_REPOSITORY_VIEW_TYPE, 1L).param("contextUri", MOCKCONTEXT_ORG_URI)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
      .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
        pathParameters(
          describeRepositoryViewContext.withParameter("type", "The type of the Repository view to be rendered as a Repository View Context."),
          describeRepositoryViewContext.withParameter("repositoryViewId", "The id of the Repository view to be rendered as a Repository View Context.")
        )
      )
    );
  }




  
  // @Test
  // @WithMockUser(roles = "ADMIN")
  // public void deleteRepositoryViewContext() throws Exception {

  //   mockMvc
  //     .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI)
  //         .contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(status().isOk())
  //     .andDo(rdh);

  // }

  // @Test
  // @WithMockUser(roles = "ADMIN")
  // public void getTriples() throws Exception {

  //   RepositoryView testRV = resositoryViewRepo.create(new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI));
  //   ApiResponse expectedResponse = new ApiResponse(ApiStatus.SUCCESS, testRV);
    
  //   mockMvc
  //     .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI+"/{id}", testRV.getId())
  //         .contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(status().isOk())
  //     .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
  //     .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
  //       pathParameters(parameterWithName("id").description("Schema id"))));

  // }

}