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

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public final class VerifyRepositoryViewSettingsControllerTest {
  private static final String CONTROLLER_PATH = "/repository-view/{type}/verify";
  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

  private static final ConstraintDescriptionsHelper describeRepositoryView = new ConstraintDescriptionsHelper(
      RepositoryView.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RepositoryViewRepo repositoryViewRepo;

  @MockBean
  private FedoraService mockFedoraService;

  private RepositoryView mockRepositoryView;

  @Before
  public void setUp() throws Exception {
      mockRepositoryView = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
      mockRepositoryView.setId(1L);

      when(repositoryViewRepo.getOne(1L)).thenReturn(mockRepositoryView);
      when(repositoryViewRepo.findOne(1L)).thenReturn(mockRepositoryView);

      ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

      Object[] args = new Object[] { mockFedoraService };

      doNothing().when(mockFedoraService).verifyPing();
      doNothing().when(mockFedoraService).verifyAuth();
      doNothing().when(mockFedoraService).verifyRoot();

      when(mockJoinPoint.getArgs()).thenReturn(args);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRVPing() throws Exception {
        mockMvc.perform(post(CONTROLLER_PATH + "/ping", RepositoryViewType.FEDORA).content(objectMapper.writeValueAsString(mockRepositoryView)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
             .andExpect(status().isOk())
             .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                 createPathParametersSnippet(),
                 createRequestFieldsSnippet(),
                 createResponseFieldsSnippet()
             ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRepositoryViewAuth() throws Exception {
        mockMvc.perform(post(CONTROLLER_PATH + "/auth", RepositoryViewType.FEDORA).content(objectMapper.writeValueAsString(mockRepositoryView)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                createPathParametersSnippet(),
                createRequestFieldsSnippet(),
                createResponseFieldsSnippet()
            ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRepositoryViewContent() throws Exception {
        mockMvc.perform(post(CONTROLLER_PATH + "/content", RepositoryViewType.FEDORA).content(objectMapper.writeValueAsString(mockRepositoryView)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                createPathParametersSnippet(),
                createRequestFieldsSnippet(),
                createResponseFieldsSnippet()
            ));
    }

    private PathParametersSnippet createPathParametersSnippet() {
        return pathParameters(
            parameterWithName("type").description("The Repository View type name.")
        );
    }

    private RequestFieldsSnippet createRequestFieldsSnippet() {
        return requestFields(
            describeRepositoryView.withField("id", "Repository View id.").optional(),
            describeRepositoryView.withField("name", "The name of this Repository View."),
            describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
            describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
            describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
            describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
            describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
            describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
            describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
        );
    }

    private ResponseFieldsSnippet createResponseFieldsSnippet() {
        return responseFields(
            fieldWithPath("meta.status").description("An enumerated string designating the success/failure of the request."),
            fieldWithPath("meta.action").description("The action associated with the status and message."),
            fieldWithPath("meta.message").description("A message associated with the response, often describing what was successful or what has failed."),
            fieldWithPath("meta.id").description("An ID associated with the meta action."),
            fieldWithPath("payload").description("The expected data, if applicable.")
        );
    }

}