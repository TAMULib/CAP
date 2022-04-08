package edu.tamu.cap.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.cap.CapApplication;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;
import edu.tamu.cap.service.repositoryview.FedoraService;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { CapApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public final class FedoraServiceTest {

    private final static String ROOT_CONTEXT_URI = "http://localhost:9100/mock/fcrepo/rest";

    private final static String TEST_VERSION1_NAME = "TestVersion1";

    private final static String TEST_VERSION2_NAME = "TestVersion2";

    private final static String TEST_CONTEXT_URI_SHORT = "path/to/container";
    private final static String TEST_CONTEXT_URI = ROOT_CONTEXT_URI + "/" + TEST_CONTEXT_URI_SHORT;

    private final static Resource TEST_SUBJECT = OWL.Class;

    private final static Property TEST_PREDICATE = RDF.type;

    private final static String TEST_OBJECT_1 = "\"TestObject1\"";

    private final static String TEST_OBJECT_2 = "\"\\\"TestObject2\\\"\"";

    private HttpServletRequest request;

    @InjectMocks
    private FedoraService fedoraService;

    private Triple mockTriple1;

    private Triple mockTriple2;

    @BeforeEach
    public void setup() throws IOException {

        request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getUserPrincipal()).thenReturn(new UsernamePasswordAuthenticationToken("aggieJack", ""));
        when(request.getAttribute(Mockito.any(String.class))).thenReturn(new HashMap<String, String>() {
            private static final long serialVersionUID = 3185237776585513150L;
            {
                put("type", "fedora");
                put("rvid", "fedora");
            }
        });

        when(request.getInputStream()).thenReturn(null);

        MockitoAnnotations.openMocks(this);

        fedoraService.setRepositoryView(new RepositoryView(RepositoryViewType.FEDORA, "Mock Fedora", "http://localhost:9100/mock/fcrepo/rest"));

        mockTriple1 = new Triple();
        mockTriple1.setSubject(TEST_SUBJECT.toString());
        mockTriple1.setPredicate(TEST_PREDICATE.toString());
        mockTriple1.setObject(TEST_OBJECT_1);

        mockTriple2 = new Triple();
        mockTriple2.setSubject(TEST_SUBJECT.toString());
        mockTriple2.setPredicate(TEST_PREDICATE.toString());
        mockTriple2.setObject(TEST_OBJECT_2);
    }

    @Test
    public void createVersion() throws Exception {
        RepositoryViewContext context = fedoraService.createVersion(TEST_CONTEXT_URI, TEST_VERSION1_NAME);
        assertVersions(context.getVersions());
    }

    @Test
    public void createVersionShortUri() throws Exception {
        RepositoryViewContext context = fedoraService.createVersion(TEST_CONTEXT_URI_SHORT, TEST_VERSION1_NAME);
        assertVersions(context.getVersions());
    }

    @Test
    public void getVersions() throws Exception {
        List<Version> versions = fedoraService.getVersions(TEST_CONTEXT_URI);
        assertVersions(versions);
    }

    @Test
    public void metadataListSize() throws Exception {
        RepositoryViewContext context = fedoraService.createMetadata(TEST_CONTEXT_URI, mockTriple1);
        int initialSize = context.getMetadata().size();

        context.addMetadatum(mockTriple1);
        context.addMetadatum(mockTriple2);

        List<Triple> metadata = context.getMetadata();

        assertEquals(initialSize + 2, metadata.size(), "Context had incorrect metadata list size!");
    }

    @Test
    public void propertyListSize() throws Exception {
        RepositoryViewContext context = fedoraService.createMetadata(TEST_CONTEXT_URI, mockTriple1);
        int initialSize = context.getProperties().size();

        context.addProperty(mockTriple1);
        context.addProperty(mockTriple2);

        List<Triple> properties = context.getProperties();

        assertEquals(initialSize + 2, properties.size(), "Context had incorrect properties list size!");
    }

    private void assertVersions(List<Version> versions) {
        assertEquals(2, versions.size(), "Context had incorrect number of versions!");

        assertEquals(TEST_VERSION1_NAME, versions.get(0).getName(), "Version 1 had the incorrect name!");
        assertEquals("2018-03-21T19:43:34.573Z^^http://www.w3.org/2001/XMLSchema#dateTime", versions.get(0).getTime(), "Version 1 had the incorrect time!");
        assertEquals(TEST_CONTEXT_URI + "/fcr:versions", versions.get(0).getTriple().getSubject(), "Version 1 had the incorrect subject!");
        assertEquals("http://fedora.info/definitions/v4/repository#hasVersion", versions.get(0).getTriple().getPredicate(), "Version 1 had the incorrect object!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions/TestVersion1", versions.get(0).getTriple().getObject(), "Version 1 had the incorrect predicate!");

        assertEquals(TEST_VERSION2_NAME, versions.get(1).getName(), "Version 2 had the incorrect name!");
        assertEquals("2018-03-21T19:44:48.852Z^^http://www.w3.org/2001/XMLSchema#dateTime", versions.get(1).getTime(), "Version 2 had the incorrect time!");
        assertEquals(TEST_CONTEXT_URI + "/fcr:versions", versions.get(1).getTriple().getSubject(), "Version 2 had the incorrect subject!");
        assertEquals("http://fedora.info/definitions/v4/repository#hasVersion", versions.get(1).getTriple().getPredicate(), "Version 2 had the incorrect object!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions/TestVersion2", versions.get(1).getTriple().getObject(), "Version 2 had the incorrect predicate!");
    }

}
