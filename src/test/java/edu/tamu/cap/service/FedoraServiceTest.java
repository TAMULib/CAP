package edu.tamu.cap.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.cap.CapApplication;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Version;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest(classes = { CapApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public final class FedoraServiceTest {

    private final static String ROOT_CONTEXT_URI = "http://localhost:9100/mock/fcrepo/rest";

    private final static String TEST_VERSION1_NAME = "TestVersion1";

    private final static String TEST_VERSION2_NAME = "TestVersion2";

    private final static String TEST_CONTEXT_URI = ROOT_CONTEXT_URI + "/path/to/container";

    private HttpServletRequest request;

    @InjectMocks
    private FedoraService fedoraService;

    @BeforeEach
    public void setup() throws IOException {

        request = mock(HttpServletRequest.class);

        Mockito.when(request.getCookies()).thenReturn(new Cookie[0]);
        when(request.getMethod()).thenReturn("POST");
        when(request.getUserPrincipal()).thenReturn(new UsernamePasswordAuthenticationToken("aggieJack", ""));
        when(request.getAttribute(any(String.class))).thenReturn(new HashMap<String, String>() {
            private static final long serialVersionUID = 3185237776585513150L;
            {
                put("type", "fedora");
                put("rvid", "fedora");
            }
        });

        when(request.getInputStream()).thenReturn(null);

        MockitoAnnotations.initMocks(this);

        fedoraService.setRepositoryView(new RepositoryView(RepositoryViewType.FEDORA, "Mock Fedora", "http://localhost:9100/mock/fcrepo/rest"));
    }

    @Test
    public void createVersion() throws Exception {
        RepositoryViewContext context = fedoraService.createVersion(TEST_CONTEXT_URI, TEST_VERSION1_NAME);
        assertVersions(context.getVersions());
    }

    @Test
    public void getVersions() throws Exception {
        List<Version> versions = fedoraService.getVersions(TEST_CONTEXT_URI);
        assertVersions(versions);
    }

    private void assertVersions(List<Version> versions) {
        assertEquals(2, versions.size(), "Context had incorrect number of versions!");

        assertEquals(TEST_VERSION1_NAME, versions.get(0).getName(), "Version 1 had the incorrect name!");
        assertEquals("2018-03-21T19:43:34.573Z^^http://www.w3.org/2001/XMLSchema#dateTime", versions.get(0).getTime(), "Version 1 had the incorrect time!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions", versions.get(0).getTriple().getSubject(), "Version 1 had the incorrect subject!");
        assertEquals("http://fedora.info/definitions/v4/repository#hasVersion", versions.get(0).getTriple().getPredicate(), "Version 1 had the incorrect object!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions/TestVersion1", versions.get(0).getTriple().getObject(), "Version 1 had the incorrect predicate!");

        assertEquals(TEST_VERSION2_NAME, versions.get(1).getName(), "Version 2 had the incorrect name!");
        assertEquals("2018-03-21T19:44:48.852Z^^http://www.w3.org/2001/XMLSchema#dateTime", versions.get(1).getTime(), "Version 2 had the incorrect time!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions", versions.get(1).getTriple().getSubject(), "Version 2 had the incorrect subject!");
        assertEquals("http://fedora.info/definitions/v4/repository#hasVersion", versions.get(1).getTriple().getPredicate(), "Version 2 had the incorrect object!");
        assertEquals("http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions/TestVersion2", versions.get(1).getTriple().getObject(), "Version 2 had the incorrect predicate!");
    }

}
