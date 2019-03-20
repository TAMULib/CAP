package edu.tamu.cap.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;
import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

public final class ReopositoryViewControllerTest {

  private static final ConstraintDescriptionsHelper describeRepositoryView = new ConstraintDescriptionsHelper(RepositoryView.class);

  private static final String REPOSITORY_VIEW_URI = "/repository-view";

  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";

  @MockBean
  private RepositoryViewRepo resositoryViewRepo;

  @MockBean
  private UserRepo userRepo;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockUserUtility mockUserUtility;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  RestDocumentationResultHandler rdh;

  @BeforeEach
  public void setUp() throws IOException {

    RepositoryView mockRV = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);
    mockRV.setId(1L);
    when(resositoryViewRepo.getOne(1L)).thenReturn(mockRV);
    when(resositoryViewRepo.findOne(1L)).thenReturn(mockRV);
    when(resositoryViewRepo.update(Mockito.any(RepositoryView.class))).thenReturn(mockRV);

    Credentials aggiejackCredentials = mockUserUtility.getMockAggieJackCredentials();
    User mockUser = new User(aggiejackCredentials.getEmail(), aggiejackCredentials.getFirstName(), aggiejackCredentials.getLastName(), aggiejackCredentials.getRole());
    when(userRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.of(mockUser));

  }

// FIXME: test not working as expected.
//  @Test
//  @WithMockUser(roles = "ADMIN")
//  public void createRepositoryView() throws Exception {
//
//    RepositoryView mockRV = resositoryViewRepo.getOne(1L);
//
//      mockMvc
//      .perform(RestDocumentationRequestBuilders.post(REPOSITORY_VIEW_URI).content(objectMapper.writeValueAsString(mockRV))
//        .contentType(MediaType.APPLICATION_JSON))
//      .andExpect(status().isOk())
//      .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//        requestFields(
//          describeRepositoryView.withField("id", "Repository View id.").ignored(),
//          describeRepositoryView.withField("name", "The name of this Repository View."),
//          describeRepositoryView.withField("rootUri", "Root URI where to the repository represented by this Repository View."),
//          describeRepositoryView.withField("type", "The Repository Type of this Repository View."),
//          describeRepositoryView.withField("username", "Optional username to use when authenticating with the repository represented by this Repository View."),
//          describeRepositoryView.withField("password", "Optional password to use when authenticating with the repository represented by this Repository View."),
//          describeRepositoryView.withField("schemas", "Optional list of Schema to resister with this Repository View."),
//          describeRepositoryView.withField("curators", "Optional list of Curators with edit permissions to this Repository View."),
//          describeRepositoryView.withField("metadataPrefixes", "Short hand references to the registered schemas.")
//        )));
//
//  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void allRepositoryViews() throws Exception {

    mockMvc
      .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void updateRepositoryView() throws Exception {

    RepositoryView mockRV = resositoryViewRepo.getOne(1L);
    mockRV.setName(TEST_REPOSITORY_VIEW_NAME+"_UPDATE");

    mockMvc
      .perform(RestDocumentationRequestBuilders.put(REPOSITORY_VIEW_URI)
          .content(objectMapper.writeValueAsString(mockRV))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deleteRepositoryView() throws Exception {

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getRepositoryView() throws Exception {

  }

}