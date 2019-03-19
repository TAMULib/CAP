package edu.tamu.cap.controller.repositoryviewcontext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.FedoraService;
import edu.tamu.cap.service.RepositoryViewType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class RepositoryViewContextChildrenControllerTest {
    private static final String CONTROLLER_PATH = "/repository-view-context/{type}/{repositoryViewId}/children";

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

    private RepositoryView mockRepositoryView;

    private RepositoryViewContext mockRepositoryViewContext;

    @Before
    public void setUp() throws Exception {
        mockRepositoryView = new RepositoryView(TEST_REPOSITORY_VIEW_TYPE, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRepositoryView.setId(1L);
        mockRepositoryView.setUsername("");
        mockRepositoryView.setPassword("");

        when(repositoryViewRepo.getOne(1L)).thenReturn(mockRepositoryView);
        when(repositoryViewRepo.findOne(1L)).thenReturn(mockRepositoryView);

        ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);
        Object[] args = new Object[] { mockFedoraService, TEST_CONTEXT_ORG_URI };
        when(mockJoinPoint.getArgs()).thenReturn(args);

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

        when(mockFedoraService.createChild(any(String.class), anyListOf(Triple.class))).thenReturn(mockRepositoryViewContext);

        mockMvc.perform(
            post(CONTROLLER_PATH, TEST_REPOSITORY_VIEW_TYPE, mockRepositoryView.getId())
                .param("contextUri", TEST_CONTEXT_ORG_URI)
                .content(objectMapper.writeValueAsString(mockTripleMap))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
        .andExpect(status().isOk());
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
        .andExpect(status().isOk());
    }
}