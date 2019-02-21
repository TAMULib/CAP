package edu.tamu.cap.controller;

import edu.tamu.cap.model.Property;
import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.SchemaRepo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir="target/generated-snippets")

public final class SchemaControllerTest {

  @MockBean
  private SchemaRepo schemaRepo;

  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  RestDocumentationResultHandler rdh;

  private static final String SCHEMA_URI = "/schema";

  private static final String TEST_SCHEMA_NAME = "TEST_SCHEMA";
  private static final String TEST_SCHEMA_NAMESPACE = "http://test-schema-location.org";
  private static final String TEST_SCHEMA_ABBREVIATION = "TS";

  private static final String TEST_SCHEMA_NAME_2 = "TEST_SCHEMA_2";
  private static final String TEST_SCHEMA_NAMESPACE_2 = "http://test-schema-2-location.org";
  private static final String TEST_SCHEMA_ABBREVIATION_2 = "TS2";

  private static final String TEST_PROPERTY_NAME_2 = "TEST_PROPERTY";
  private static final String TEST_PROPERTY_NAMESPACE_2 = "http://test-property-location.org";


  @Before
  public void setUp() throws JsonProcessingException {



  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void createSchema() throws Exception {

    List<Property> properties = new ArrayList<Property>();

    properties.add(new Property());

    Schema testSchema = new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION);

    mockMvc
        .perform(RestDocumentationRequestBuilders
            .post(SCHEMA_URI)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(testSchema)))
      .andExpect(status().isOk())
      .andDo(document("{method-name}/", 
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()),  
        requestFields(
        fieldWithPath("id").description("Schema id").ignored(),
        fieldWithPath("name").description("Schema name"),
        fieldWithPath("abbreviation").description("Schema abbreviation"),
        fieldWithPath("namespace").description("Schema namespace"),
        fieldWithPath("properties").description("Schema properties")
      )));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void allSchemas() throws Exception {

    schemaRepo.create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));
    schemaRepo.create(new Schema(TEST_SCHEMA_NAME_2, TEST_SCHEMA_NAMESPACE_2, TEST_SCHEMA_ABBREVIATION_2));

    mockMvc
        .perform(RestDocumentationRequestBuilders
            .get(SCHEMA_URI)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getSchema() throws Exception {

    Schema testSchema = schemaRepo.create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));

    mockMvc
        .perform(RestDocumentationRequestBuilders
            .get(SCHEMA_URI)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(rdh);
  }

  @TestConfiguration
  static class CustomizationConfiguration implements RestDocsMockMvcConfigurationCustomizer {
      @Override
      public void customize(MockMvcRestDocumentationConfigurer configurer) {
        
        
      }
      @Bean
      public RestDocumentationResultHandler restDocumentation() {
          return MockMvcRestDocumentation.document("{method-name}", 
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()));
      }
  }

}