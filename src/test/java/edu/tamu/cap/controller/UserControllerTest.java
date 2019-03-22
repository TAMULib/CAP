package edu.tamu.cap.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.cap.utility.ConstraintDescriptionsHelper;
import edu.tamu.cap.utility.MockUserUtility;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public final class UserControllerTest {
  private static final String CONTROLLER_PATH = "/user";

  private static final ConstraintDescriptionsHelper describeUser = new ConstraintDescriptionsHelper(User.class);
  private static final ConstraintDescriptionsHelper describeCredentials = new ConstraintDescriptionsHelper(Credentials.class);

  private static FieldDescriptor[] metaDescriptor = new FieldDescriptor[] {
      fieldWithPath("meta.status").description("An enumerated string designating the success/failure of the request."),
      fieldWithPath("meta.action").description("The action associated with the status and message."),
      fieldWithPath("meta.message").description("A message associated with the response, often describing what was successful or what has failed."),
      fieldWithPath("meta.id").description("An ID associated with the meta action.")
 };

  private static FieldDescriptor[] userDescriptor = new FieldDescriptor[] {
      describeUser.withField("id", "An ID."),
      describeUser.withField("username", "The username."),
      describeUser.withField("role", "The role."),
      describeUser.withField("firstName", "The first name."),
      describeUser.withField("lastName", "The last name."),
      describeUser.withField("email", "The e-mail address.")
 };

  private static FieldDescriptor[] credentialsDescriptor = new FieldDescriptor[] {
      describeUser.withField("firstName", "The first name."),
      describeUser.withField("lastName", "The last name."),
      describeUser.withField("netId", "The Net-ID."),
      describeUser.withField("exp", ""),
      describeUser.withField("email", "The e-mail address."),
      describeUser.withField("role", "The access role."),
      describeUser.withField("affiliation", "An affiliation name."),
      describeUser.withField("allCredentials", "All related credentials.")
  };

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockUserUtility mockUserUtility;

  @MockBean
  private UserRepo userRepo;

  private Credentials mockAggieJackCredentials;

  private User mockAggieJack;

  private User mockAggieJane;

  private List<User> mockUsers;

  @Before
  public void setUp() throws Exception {
      mockAggieJackCredentials = mockUserUtility.getMockAggieJackCredentials();
      mockAggieJack = mockUserUtility.getMockAggieJackUser();
      mockAggieJack.setId(1L);

      mockAggieJane = mockUserUtility.getMockAggieJackUser();
      mockAggieJane.setId(2L);

      mockUsers = new ArrayList<User>();
      mockUsers.add(mockAggieJack);
      mockUsers.add(mockAggieJane);

      when(userRepo.findAll()).thenReturn(mockUsers);
      when(userRepo.update(mockAggieJack)).thenReturn(mockAggieJack);

      doNothing().when(userRepo).delete(any(User.class));
    }

// FIXME: needs more work, currently gives credentials not found (HTTP 401 and then HTTP 500).
//    @Test
//    @WithMockUser(roles = "USER")
//    public void credentials() throws Exception {
//        mockMvc.perform(
//            post(CONTROLLER_PATH + "/credentials")
//                .content(objectMapper.writeValueAsString(mockAggieJackCredentials))
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//        )
//        .andExpect(status().isOk())
//        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//            requestFields(credentialsDescriptor)
//        ));
//    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void allUsers() throws Exception {
        mockMvc.perform(
            get(CONTROLLER_PATH + "/all")
        )
        .andExpect(status().isOk())
// FIXME: andWithPrefix() not working as expected when following documentation.
//        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//            responseFields(fieldWithPath("meta").description("The meta data.")),
//            responseFields(metaDescriptor),
//            responseFields(
//                fieldWithPath("payload").description("Container for the list of Users."),
//                fieldWithPath("payload['ArrayList<User>']").description("List of users.")
//            ),
//            requestFields(userDescriptor)
//                .andWithPrefix("payload['ArrayList<User>'][].", userDescriptor)
//        ));
        ;
    }

// FIXME: This test is throwing a nested serverlet exception. 
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     public void update() throws Exception {
//         mockMvc.perform(
//             get(CONTROLLER_PATH + "/update")
//                 .content(objectMapper.writeValueAsString(mockAggieJack))
//                 .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//         )
//         .andExpect(status().isOk())
// // FIXME: andWithPrefix() not working as expected when following documentation.
// //        .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
// //            responseFields(fieldWithPath("meta").description("The meta data.")),
// //            responseFields(metaDescriptor),
// //            responseFields(
// //                fieldWithPath("payload").description("Container for the updated User."),
// //                fieldWithPath("payload.User").description("The updated user.")
// //            ),
// //            requestFields(userDescriptor).andWithPrefix("payload.User.", userDescriptor)
// //        ));
//         ;
//     }

     @Test
     @WithMockUser(roles = "ADMIN")
     public void delete() throws Exception {
         mockMvc.perform(
             get(CONTROLLER_PATH + "/delete")
                 .content(objectMapper.writeValueAsString(mockAggieJack))
                 .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
         )
         .andExpect(status().isOk())
// FIXME: andWithPrefix() not working as expected when following documentation.
//         .andDo(document("{method-name}/", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//             requestFields(userDescriptor).andWithPrefix("payload.User.", userDescriptor)
//         ));
         ;
     }

}