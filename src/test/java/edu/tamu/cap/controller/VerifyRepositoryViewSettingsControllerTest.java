package edu.tamu.cap.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.service.repositoryview.FedoraService;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public final class VerifyRepositoryViewSettingsControllerTest {
  private static final String CONTROLLER_PATH = "/repository-view/{type}/verify";

  private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

  private static final ConstraintDescriptionsHelper describeRepositoryView = new ConstraintDescriptionsHelper(RepositoryView.class);

  private static final FieldDescriptor[] repositoryViewDescriptor = new FieldDescriptor[] {
      describeRepositoryView.withField("id", "Repository View id.").optional(),
      describeRepositoryView.withField("name", "The name of this Repository View."),
      describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
      describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
      describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
      describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
      describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
      describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
      describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
  };

  private static final ParameterDescriptor[] urlPathDescriptor = new ParameterDescriptor[] {
      parameterWithName("type").description("The Repository View type name.")
  };

  private static final FieldDescriptor[] responseDescriptor = new FieldDescriptor[] {
      fieldWithPath("meta.status").description("An enumerated string designating the success/failure of the request."),
      fieldWithPath("meta.action").description("The action associated with the status and message."),
      fieldWithPath("meta.message").description("A message associated with the response, often describing what was successful or what has failed."),
      fieldWithPath("meta.id").description("An ID associated with the meta action."),
      fieldWithPath("payload").description("The expected data, if applicable.")
  };

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RepositoryViewRepo repositoryViewRepo;

  @MockBean
  private FedoraService mockFedoraService;

  private RepositoryView mockRepositoryView;

  @BeforeEach
  public void setUp() throws Exception {
      mockRepositoryView = new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
      mockRepositoryView.setId(1L);
      mockRepositoryView.setUsername("");
      mockRepositoryView.setPassword("");

      when(repositoryViewRepo.getById(mockRepositoryView.getId())).thenReturn(mockRepositoryView);
      when(repositoryViewRepo.findById(mockRepositoryView.getId())).thenReturn(Optional.of(mockRepositoryView));

      ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

      Object[] args = new Object[] { mockFedoraService };

      when(mockJoinPoint.getArgs()).thenReturn(args);

      doNothing().when(mockFedoraService).verifyPing();
      doNothing().when(mockFedoraService).verifyAuth();
      doNothing().when(mockFedoraService).verifyRoot();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRVPing() throws Exception {
        mockMvc.perform(
            post(CONTROLLER_PATH + "/ping", TEST_REPOSITORY_VIEW_TYPE)
                .content(objectMapper.writeValueAsString(mockRepositoryView))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor),
            requestFields(repositoryViewDescriptor),
            responseFields(responseDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRepositoryViewAuth() throws Exception {
        mockMvc.perform(
            post(CONTROLLER_PATH + "/auth", TEST_REPOSITORY_VIEW_TYPE)
                .content(objectMapper.writeValueAsString(mockRepositoryView))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor),
            requestFields(repositoryViewDescriptor),
            responseFields(responseDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRepositoryViewContent() throws Exception {
        mockMvc.perform(
            post(CONTROLLER_PATH + "/content", TEST_REPOSITORY_VIEW_TYPE)
                .content(objectMapper.writeValueAsString(mockRepositoryView))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor),
            requestFields(repositoryViewDescriptor),
            responseFields(responseDescriptor)
        ));
    }

}