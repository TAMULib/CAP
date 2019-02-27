package edu.tamu.cap.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

public final class ReopositoryViewControllerTest {

  private static final String REPOSITORY_VIEW_URI = "/repository-view";

  private static final String TEST_REPOSITORY_VIEW_NAME = "TEST_REPOSITORY_VIEW_NAME";
  private static final String TEST_REPOSITORY_VIEW_URI = "http://test-repository-view.org";


  @Autowired
  private RepositoryViewRepo resositoryViewRepo;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  RestDocumentationResultHandler rdh;

  @Before
  public void setUp() throws JsonProcessingException {

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void createRepositoryView() throws Exception {

    RepositoryView testRV = new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI);

    mockMvc
      .perform(RestDocumentationRequestBuilders.post(REPOSITORY_VIEW_URI).content(objectMapper.writeValueAsString(testRV))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
        requestFields(
          fieldWithPath("id").description("Repository View id").ignored(),
          fieldWithPath("name").description("The name of this Repository View."),
          fieldWithPath("rootUri").description("Root URI where to the repository represented by this Repository View."),
          fieldWithPath("type").description("The Repository Type of this Repository View"),
          fieldWithPath("username").description("Optional username to use when authenticating with the repository represented by this Repository View"),
          fieldWithPath("password").description("Optional password to use when authenticating with the repository represented by this Repository View"),
          fieldWithPath("schemas").description("Optional list of Schema to resister with this Repository View"),
          fieldWithPath("curators").description("Optional list of Curators with edit permissions to this Repository View"),
          fieldWithPath("metadataPrefixes").description("")
        )));

  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void allRepositoryViews() throws Exception {

    mockMvc
      .perform(RestDocumentationRequestBuilders.get(REPOSITORY_VIEW_URI)
          .contentType(MediaType.APPLICATION_JSON))
      //.andExpect(status().isOk())
      .andDo(rdh);

  }

  @Test
  @WithMockUser(roles = "CURATOR")
  public void updateRepositoryView() throws Exception {

    RepositoryView testRV = resositoryViewRepo.create(new RepositoryView(RepositoryViewType.FEDORA, TEST_REPOSITORY_VIEW_NAME, TEST_REPOSITORY_VIEW_URI));

    testRV.setName(TEST_REPOSITORY_VIEW_NAME+"_UPDATE");

    mockMvc
      .perform(RestDocumentationRequestBuilders.put(REPOSITORY_VIEW_URI)
          .content(objectMapper.writeValueAsString(testRV))
          .contentType(MediaType.APPLICATION_JSON))
      //.andExpect(status().isOk())
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