package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repositoryviewcontext.TransactionDetails;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextTransactionControllerTest {
    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}/transaction";

    private static final RepositoryViewType TEST_REPOSITORY_VIEW_TYPE = RepositoryViewType.FEDORA;
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final String TEST_CONTEXT_ORG_URI = "http://example.com";

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RepositoryViewRepo repositoryViewRepo;

    @MockBean
    private FedoraService mockFedoraService;

    @MockBean
    private RepositoryViewContextTransactionController mockRepositoryViewContextTransactionController;

    private RepositoryView mockRepositoryView;

    @Before
    public void setUp() throws Exception {
        mockRepositoryView = new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRepositoryView.setId(1L);
        mockRepositoryView.setUsername("");
        mockRepositoryView.setPassword("");

        when(repositoryViewRepo.getOne(mockRepositoryView.getId())).thenReturn(mockRepositoryView);
        when(repositoryViewRepo.findOne(mockRepositoryView.getId())).thenReturn(mockRepositoryView);

        ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);
        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockJoinPoint.getArgs()).thenReturn(args);

        RepositoryViewContext mockRepositoryViewContext = new RepositoryViewContext();
        when(mockFedoraService.getRepositoryViewContext(any(String.class))).thenReturn(mockRepositoryViewContext);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void startTransaction() throws Exception {
        TransactionDetails mockTransactionDetails = mockFedoraService.makeTransactionDetails("mock transaction details", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
        when(mockFedoraService.startTransaction()).thenReturn(mockTransactionDetails);
        doNothing().when(mockFedoraService).commitTransaction(any(String.class));

        when(mockFedoraService.refreshTransaction(any(String.class))).thenReturn(mockTransactionDetails);

        mockMvc.perform(
            get(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
        )
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void rollbackTransaction() throws Exception {
        doNothing().when(mockFedoraService).commitTransaction(any(String.class));

        mockMvc.perform(
            delete(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void refreshTransaction() throws Exception {
        TransactionDetails mockTransactionDetails = mockFedoraService.makeTransactionDetails("mock transaction details", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
        when(mockFedoraService.refreshTransaction(any(String.class))).thenReturn(mockTransactionDetails);

        mockMvc.perform(
            put(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
        )
        .andExpect(status().isOk());
    }
}