package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextControllerTest {

    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}";

    private static final ConstraintDescriptionsHelper describeRepositoryViewContext = new ConstraintDescriptionsHelper(RepositoryViewContext.class);

    private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final String TEST_CONTEXT_ORG_URI = "http://example.com";
    private static final Triple TEST_TRIPLE = new Triple("TestSubject", "TestPredicate", "TestObject");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RepositoryViewRepo repositoryViewRepo;

    @MockBean
    private FedoraService mockFedoraService;

    @MockBean
    private ProceedingJoinPoint mockProceedingJoinPoint;

    private RepositoryView mockRepositoryView;

    private RepositoryViewContext mockRepositoryViewContext;

    @BeforeEach
    public void setUp() throws Exception {
        mockRepositoryView = new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRepositoryView.setId(1L);
        mockRepositoryView.setUsername("");
        mockRepositoryView.setPassword("");

        when(repositoryViewRepo.getOne(1L)).thenReturn(mockRepositoryView);
        when(repositoryViewRepo.findOne(1L)).thenReturn(mockRepositoryView);

        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockProceedingJoinPoint.getArgs()).thenReturn(args);

        mockRepositoryViewContext = new RepositoryViewContext();
        when(mockFedoraService.getRepositoryViewContext(any(String.class))).thenReturn(mockRepositoryViewContext);
    }

    @AfterEach
    public void tearDown() {
        // resositoryViewRepo.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getRepositoryViewContext() throws Exception {
        mockMvc.perform(
            get(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
            )
            .andExpect(status().isOk())
            .andDo(
                document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    pathParameters(
                        describeRepositoryViewContext.withParameter("type", "The type of the Repository view to be rendered as a Repository View Context."),
                        describeRepositoryViewContext.withParameter("repositoryViewId", "The id of the Repository view to be rendered as a Repository View Context.")
                    )
                )
            );
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteRepositoryViewContext() throws Exception {
        doNothing().when(mockFedoraService).deleteRepositoryViewContext(any(String.class));

        mockMvc.perform(
            delete(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
                .content(objectMapper.writeValueAsString(mockRepositoryView))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getTriples() throws Exception {
        List<Triple> mockTriples = new ArrayList<Triple>();
        mockTriples.add(TEST_TRIPLE);
        when(mockFedoraService.getTriples(any(FedoraService.class), any(String.class))).thenReturn(mockTriples);

        mockMvc.perform(
            get(CONTROLLER_PATH + "/triples", TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
            )
            .andExpect(status().isOk())
            .andDo(
                document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    pathParameters(
                        describeRepositoryViewContext.withParameter("type", "The type of the Repository view to be rendered as a Repository View Context."),
                        describeRepositoryViewContext.withParameter("repositoryViewId", "The id of the Repository view to be rendered as a Repository View Context.")
                    )
                )
            );
    }
}