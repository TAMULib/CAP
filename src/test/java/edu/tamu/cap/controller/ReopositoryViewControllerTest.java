// package edu.tamu.cap.controller;

// import static edu.tamu.cap.service.RepositoryViewType.FEDORA;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;

// import edu.tamu.cap.model.RepositoryView;
// import edu.tamu.cap.model.Schema;
// import edu.tamu.cap.model.repo.RepositoryViewRepo;
// import edu.tamu.weaver.response.ApiResponse;



// @ExtendWith(MockitoExtension.class)
// @ExtendWith(SpringExtension.class)
// public final class ReopositoryViewControllerTest {

//     @InjectMocks
//     private RepositoryViewController repositoryViewController;

//     @Mock
//     private RepositoryViewRepo repositoryViewRepo;

//     private RepositoryView fedoraRepositoryView;

//     @BeforeEach
//     public void setup() throws JsonParseException, JsonMappingException, IOException {
//         MockitoAnnotations.initMocks(this);

//         fedoraRepositoryView = getMockFedoraRepositoryView();
//     }

//     @Test
//     public void testCreateRepositoryView() {
//         RepositoryView responseRV = null;

//         when(repositoryViewRepo.create(any(RepositoryView.class))).thenReturn(fedoraRepositoryView);

//         ApiResponse response = repositoryViewController.createRepositoryView(fedoraRepositoryView);
//         Collection<Object> payload = response.getPayload().values();
//         if (payload.size() == 1) {
//             responseRV = (RepositoryView) payload.toArray()[0];
//         }

//         assertEquals(fedoraRepositoryView.getId(), responseRV.getId(), "Fedora Repository View has incorrect ID!");
//         assertEquals(fedoraRepositoryView.getName(), responseRV.getName(), "Fedora Repository View has incorrect Name!");
//         assertEquals(fedoraRepositoryView.getRootUri(), responseRV.getRootUri(), "Fedora Repository View has incorrect Root URI!");
//         assertEquals(fedoraRepositoryView.getSchemas(), responseRV.getSchemas(), "Fedora Repository View has incorrect Schemas!");
//         assertEquals(fedoraRepositoryView.getType(), responseRV.getType(), "Fedora Repository View has incorrect Type!");
//         assertEquals(fedoraRepositoryView.getUsername(), responseRV.getUsername(), "Fedora Repository View has incorrect Username!");
//         assertEquals(fedoraRepositoryView.getPassword(), responseRV.getPassword(), "Fedora Repository View has incorrect Password!");
//     }

//     private RepositoryView getMockFedoraRepositoryView() {
//         // TODO: convert this into json file to be imported.
//         List<Schema> schemas = new ArrayList<Schema>();
//         Schema schema = new Schema();
//         schema.setId(1L);
//         schema.setName("Schema Name");
//         schema.setNamespace("Schema Namespace");
//         schema.setAbbreviation("Schema Abbreviation");
//         schemas.add(schema);

//         RepositoryView rv = new RepositoryView();
//         rv.setId(123456789L);
//         rv.setName("Repository View Name");
//         rv.setRootUri("http://localhost/fedora");
//         rv.setSchemas(schemas);
//         rv.setType(FEDORA);
//         rv.setUsername("");
//         rv.setPassword("");
//         return rv;
//     }

// }
