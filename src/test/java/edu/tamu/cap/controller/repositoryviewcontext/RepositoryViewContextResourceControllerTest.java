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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.FixityReport;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextResourceControllerTest {

    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}/resource";

    private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final String TEST_CONTEXT_ORG_URI = "http://example.com";

    private static final String TEST_FIXITY_REPORT_MESSAGE_DIGEST = "Test Message Digest";
    private static final String TEST_FIXITY_REPORT_SIZE = "Test Size";
    private static final String TEST_FIXITY_REPORT_STATUS = "Test Status";

    private static final ParameterDescriptor[] urlPathDescriptor = new ParameterDescriptor[] {
        parameterWithName("type").description("The type of the Repository view to be rendered as a Repository View Context."),
        parameterWithName("repositoryViewId").description("The id of the Repository view to be rendered as a Repository View Context.")
    };

    @Autowired
    private MockMvc mockMvc;

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

        when(repositoryViewRepo.getOne(mockRepositoryView.getId())).thenReturn(mockRepositoryView);
        when(repositoryViewRepo.findOne(mockRepositoryView.getId())).thenReturn(mockRepositoryView);

        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockProceedingJoinPoint.getArgs()).thenReturn(args);

        mockRepositoryViewContext = new RepositoryViewContext();
        when(mockFedoraService.getRepositoryViewContext(any(String.class))).thenReturn(mockRepositoryViewContext);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createResource() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock_file.txt", "text/plain", "mock data".getBytes());
        when(mockFedoraService.createResource(any(String.class), any(MultipartFile.class))).thenReturn(mockRepositoryViewContext);

        mockMvc.perform(
            fileUpload(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .file(mockMultipartFile)
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk());
// FIXME: java.lang.IllegalArgumentException: urlTemplate not found. If you are using MockMvc did you use RestDocumentationRequestBuilders to build the request?
//        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//            pathParameters(urlPathDescriptor)
//        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getResources() throws Exception {
        when(mockFedoraService.getResource(any(String.class))).thenReturn(mockRepositoryViewContext);

        mockMvc.perform(
            get(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteResources() throws Exception {
        doNothing().when(mockFedoraService).deleteResource(any(String.class));

        mockMvc.perform(
            delete(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void checkFixity() throws Exception {
        FixityReport mockFixityReport = new FixityReport();
        mockFixityReport.setMessageDigest(TEST_FIXITY_REPORT_MESSAGE_DIGEST);
        mockFixityReport.setSize(TEST_FIXITY_REPORT_SIZE);
        mockFixityReport.setStatus(TEST_FIXITY_REPORT_STATUS);
        when(mockFedoraService.resourceFixity(any(String.class))).thenReturn(mockFixityReport);

        mockMvc.perform(
            get(CONTROLLER_PATH + "/fixity", TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor)
        ));
    }
}