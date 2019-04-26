package edu.tamu.cap.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class ResourceProxyControllerTest {

    private static final String RESOURCE_PROXY_URI = "/resource-proxy";

    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    @MockBean
    private RepositoryViewRepo repositoryViewRepo;

    @Autowired
    private MockMvc mockMvc;

    private RepositoryView mockRV;

    @BeforeEach
    public void setUp() throws IOException {
        mockRV = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRV.setId(1L);
        List<RepositoryView> repositoryViews = new ArrayList<RepositoryView>();
        repositoryViews.add(mockRV);
        when(repositoryViewRepo.findByRootUriContainingIgnoreCase(TEST_REPOSITORY_VIEW_URI)).thenReturn(repositoryViews);
    }

    @Test
    public void unknownSource() throws Exception {
        mockMvc.perform(
                get(RESOURCE_PROXY_URI)
                    .param("uri", "http://unknown.domain.com")
            )
            .andExpect(status().is(403));
    }

    @Test
    public void badUri() throws Exception {
        mockMvc.perform(
                get(RESOURCE_PROXY_URI)
                    .param("uri", "notaurl")
            )
            .andExpect(status().is(400));
    }

}