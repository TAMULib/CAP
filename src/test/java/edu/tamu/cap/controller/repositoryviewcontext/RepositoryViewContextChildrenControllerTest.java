package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.service.repositoryview.FedoraService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextChildrenControllerTest {

    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}/children";

    private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final String TEST_CONTEXT_ORG_URI = "http://example.com";
    private static final Triple TEST_TRIPLE = new Triple("TestSubject", "TestPredicate", "TestObject");

    private static final ParameterDescriptor[] urlPathDescriptor = new ParameterDescriptor[] {
        parameterWithName("type").description("The type of the Repository view to be rendered as a Repository View Context."),
        parameterWithName("repositoryViewId").description("The id of the Repository view to be rendered as a Repository View Context.")
    };

    private static final ParameterDescriptor[] contextUriDescriptor = new ParameterDescriptor[] {
        parameterWithName("contextUri").description("The URI resource within the designated repository.")
    };

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

        when(repositoryViewRepo.getById(1L)).thenReturn(mockRepositoryView);
        when(repositoryViewRepo.findById(1L)).thenReturn(Optional.of(mockRepositoryView));

        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockProceedingJoinPoint.getArgs()).thenReturn(args);

        mockRepositoryViewContext = new RepositoryViewContext();
        when(mockFedoraService.getRepositoryViewContext(any(String.class))).thenReturn(mockRepositoryViewContext);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void create() throws Exception {
        List<Triple> mockTriples = new ArrayList<Triple>();
        mockTriples.add(TEST_TRIPLE);

        HashMap<String, String> mockHashMap = new HashMap<String, String>();
        mockHashMap.put("subject", TEST_TRIPLE.getSubject());
        mockHashMap.put("predicate", TEST_TRIPLE.getPredicate());
        mockHashMap.put("object", TEST_TRIPLE.getObject());

        ArrayList<HashMap<String, String>> mockTripleMap = new ArrayList<HashMap<String, String>>(
            Arrays.asList(mockHashMap)
        );

        when(mockFedoraService.createChild(any(String.class), anyList())).thenReturn(mockRepositoryViewContext);

        mockMvc.perform(
            post(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
                .content(objectMapper.writeValueAsString(mockTripleMap))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor),
            requestParameters(contextUriDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getContainer() throws Exception {
        List<Triple> mockTriples = new ArrayList<Triple>();
        mockTriples.add(TEST_TRIPLE);
        when(mockFedoraService.getChildren(any(String.class))).thenReturn(mockTriples);

        mockMvc.perform(
            get(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor),
            requestParameters(contextUriDescriptor)
        ));
    }
}