package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repositoryviewcontext.FedoraTransactionDetails;
import edu.tamu.cap.model.repositoryviewcontext.TransactionDetails;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextTransactionControllerTest {

    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}/transaction";

    private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_CONTEXT_ORG_URI = "http://example.com?token=tx:" + TEST_TOKEN;

    private static final ParameterDescriptor[] urlPathDescriptor = new ParameterDescriptor[] {
        parameterWithName("type").description("The Repository View type name."),
        parameterWithName("repositoryViewId").description("The Repository View identifier.")
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

    private FedoraTransactionDetails mockTransactionDetails;

    @BeforeEach
    public void setUp() throws Exception {
        mockRepositoryView = new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRepositoryView.setId(1L);
        mockRepositoryView.setUsername("");
        mockRepositoryView.setPassword("");

        when(repositoryViewRepo.getOne(any())).thenReturn(mockRepositoryView);
        when(mockFedoraService.getRepositoryView()).thenReturn(mockRepositoryView);

        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockProceedingJoinPoint.getArgs()).thenReturn(args);

        RepositoryViewContext mockRepositoryViewContext = new RepositoryViewContext();
        when(mockFedoraService.getRepositoryViewContext(any())).thenReturn(mockRepositoryViewContext);

        mockTransactionDetails = new FedoraTransactionDetails(TEST_TOKEN, DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
        when(mockFedoraService.refreshTransaction(any())).thenReturn(mockTransactionDetails);

        doNothing().when(mockFedoraService).commitTransaction(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void startTransaction() throws Exception {
        when(mockFedoraService.startTransaction()).thenReturn(mockTransactionDetails);

        mockMvc.perform(
            get(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor)
        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void rollbackTransaction() throws Exception {
        doNothing().when(mockFedoraService).rollbackTransaction(any());

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
    public void refreshTransaction() throws Exception {
        mockMvc.perform(
            put(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            pathParameters(urlPathDescriptor)
        ));
    }
}
