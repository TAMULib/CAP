package edu.tamu.cap.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "/target/generated-snippets")
public final class VerifyRepositoryViewSettingsControllerTest {
    private static final String TEST_REPOSITORY_VIEW_TYPE = "repository-view/{type}/verify";
    private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
    private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

    private static final ConstraintDescriptionsHelper describeRepositoryView = new ConstraintDescriptionsHelper(RepositoryView.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RestDocumentationResultHandler rdh;

    @MockBean
    private RepositoryViewRepo resositoryViewRepo;

    private RepositoryView mockRV;

    @Before
    public void setUp() throws JsonProcessingException {
        mockRV = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
        mockRV.setId(1L);

        when(resositoryViewRepo.getOne(1L)).thenReturn(mockRV);
        when(resositoryViewRepo.findOne(1L)).thenReturn(mockRV);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void verifyRVPing() throws Exception {
        //mockMvc.perform(post(TEST_REPOSITORY_VIEW_TYPE + "/ping", mockRV.getType().name()).content(objectMapper.writeValueAsString(mockRV)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        mockMvc.perform(post("/repository-view/FEDORA/verify/ping", mockRV.getType().name()).content(objectMapper.writeValueAsString(mockRV)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            .andExpect(status().isOk())
//            .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//                pathParameters(
//                    parameterWithName("type").description("The Repository View type name.")
//                ),
//                requestFields(
//                    describeRepositoryView.withField("id", "Repository View id.").optional(),
//                    describeRepositoryView.withField("name", "The name of this Repository View."),
//                    describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
//                    describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
//                    describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
//                    describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
//                    describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
//                    describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
//                    describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
//                )
//            ))
            ;
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void verifyRepositoryViewAuth() throws Exception {
//        mockMvc
//            .perform(post(TEST_REPOSITORY_VIEW_TYPE + "/auth")
//                .content(objectMapper.writeValueAsString(mockRV))
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//            )
//            .andExpect(status().isOk())
//            .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//                requestFields(
//                    describeRepositoryView.withField("id", "Repository View id.").optional(),
//                    describeRepositoryView.withField("name", "The name of this Repository View."),
//                    describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
//                    describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
//                    describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
//                    describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
//                    describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
//                    describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
//                    describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
//                )
//            ));
//    }

}