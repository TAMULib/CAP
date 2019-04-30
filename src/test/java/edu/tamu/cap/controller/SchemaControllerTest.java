package edu.tamu.cap.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.tamu.cap.model.Property;
import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.SchemaRepo;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir="target/generated-snippets")

public final class SchemaControllerTest {

  @Autowired
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

  private static final String TEST_PROPERTY_LABEL = "TEST_PROPERTY_LABEL";
  private static final String TEST_PROPERTY_URI = "http://test-property-uri.org";

  private static final String TEST_PROPERTY_2_LABEL = "TEST_PROPERTY_LABEL_2";
  private static final String TEST_PROPERTY_2_URI = "http://test-property-2-uri.org";

  private static final String PROPERTIES_URI = SCHEMA_URI + "/properties?namespace=";

  @Before
  public void setUp() throws JsonProcessingException {

    // We should be mocking responses
    // Schema testSchema = new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE,
    // TEST_SCHEMA_ABBREVIATION);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void createSchema() throws Exception {

    List<Property> properties = new ArrayList<Property>();

    Property property = new Property();
    property.setLabel(TEST_PROPERTY_LABEL);
    property.setUri(TEST_PROPERTY_URI);
    properties.add(property);

    Property property2 = new Property();
    property.setLabel(TEST_PROPERTY_2_LABEL);
    property.setUri(TEST_PROPERTY_2_URI);
    properties.add(property2);

    Schema testSchema = new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION, properties);
    

    ApiResponse expectedResponse = new ApiResponse(ApiStatus.SUCCESS, testSchema);

    mockMvc
        .perform(RestDocumentationRequestBuilders.post(SCHEMA_URI).content(objectMapper.writeValueAsString(testSchema))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            requestFields(fieldWithPath("id").description("Schema id").ignored(),
                fieldWithPath("name").description("Schema name"),
                fieldWithPath("abbreviation").description("Schema abbreviation"),
                fieldWithPath("namespace").description("Schema namespace"),
                fieldWithPath("properties").description("Schema properties"),
                fieldWithPath("namespaces").description("Included namespaces"))));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void allSchemas() throws Exception {

    schemaRepo.create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));
    schemaRepo.create(new Schema(TEST_SCHEMA_NAME_2, TEST_SCHEMA_NAMESPACE_2, TEST_SCHEMA_ABBREVIATION_2));

    mockMvc.perform(RestDocumentationRequestBuilders.get(SCHEMA_URI).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(rdh);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void getSchema() throws Exception {

    Schema testSchema = schemaRepo
        .create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));
    ApiResponse expectedResponse = new ApiResponse(ApiStatus.SUCCESS, testSchema);

    mockMvc
        .perform(RestDocumentationRequestBuilders.get(SCHEMA_URI + "/{id}", testSchema.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
        .andDo(document("{method-name}/",
            pathParameters(parameterWithName("id").description("The id of the Schema to be retrieved."))));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void updateSchema() throws Exception {

    Schema testSchema = schemaRepo
        .create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));

    testSchema.setName(TEST_SCHEMA_NAME + "_UPDATED");
    ApiResponse expectedResponse = new ApiResponse(ApiStatus.SUCCESS, testSchema);

    mockMvc
        .perform(RestDocumentationRequestBuilders.put(SCHEMA_URI, testSchema.getId())
            .content(objectMapper.writeValueAsString(testSchema)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
        .andDo(rdh);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deleteSchema() throws Exception {

    Schema testSchema = schemaRepo
        .create(new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION));

    testSchema.setName(TEST_SCHEMA_NAME + "_DELETE");

    mockMvc
        .perform(RestDocumentationRequestBuilders.delete(SCHEMA_URI, testSchema.getId())
            .content(objectMapper.writeValueAsString(testSchema)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(rdh);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void propertiesByNamespace() throws Exception {

    List<Property> properties = new ArrayList<Property>();

    Property property = new Property();
    property.setLabel(TEST_PROPERTY_LABEL);
    property.setUri(TEST_PROPERTY_URI);
    properties.add(property);

    Property property2 = new Property();
    property.setLabel(TEST_PROPERTY_2_LABEL);
    property.setUri(TEST_PROPERTY_2_URI);
    properties.add(property2);

    Schema testSchema = new Schema(TEST_SCHEMA_NAME, TEST_SCHEMA_NAMESPACE, TEST_SCHEMA_ABBREVIATION, properties);

    mockMvc
        .perform(RestDocumentationRequestBuilders
          .get(PROPERTIES_URI+testSchema.getProperties().get(0).getUri())
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(document("{method-name}/", 
          preprocessRequest(prettyPrint()), 
          preprocessResponse(prettyPrint()),
          requestParameters(
            parameterWithName("namespace").description("The namespace URI of the desired Property.")
        )));

  }

}