package edu.tamu.cap.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.cap.CapApplication;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CapApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class FedoraServiceTest {

    private final static String ROOT_CONTEXT_URI = "http://localhost:9100/mock/fcrepo/rest";

    private final static String TEST_VERSION1_NAME = "TestVersion1";

    private final static String TEST_CONTEXT_URI = ROOT_CONTEXT_URI + "/path/to/container";

    @Autowired
    private FedoraService fedoraService;

    @Before
    public void setup() {
        fedoraService.setIr(new IR(IRType.FEDORA, "Mock Fedora", "http://localhost:9100/mock/fcrepo/rest"));
    }

    @Test
    public void createVersion() throws Exception {
        IRContext context = fedoraService.createVersion(TEST_CONTEXT_URI, TEST_VERSION1_NAME);
        assertEquals("Context had incorrect number of versions!", 2, context.getVersions().size());
        assertEquals("Version had the incorrect name!", TEST_VERSION1_NAME, context.getVersions().get(0).getName());
        assertEquals("Version had the incorrect time!", "2018-03-21T19:43:34.573Z^^http://www.w3.org/2001/XMLSchema#dateTime", context.getVersions().get(0).getTime());
        assertEquals("Version had the incorrect subject!", "http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions", context.getVersions().get(0).getTriple().getSubject());
        assertEquals("Version had the incorrect object!", "http://fedora.info/definitions/v4/repository#hasVersion", context.getVersions().get(0).getTriple().getPredicate());
        assertEquals("Version had the incorrect predicate!", "http://localhost:9100/mock/fcrepo/rest/path/to/container/fcr:versions/TestVersion1", context.getVersions().get(0).getTriple().getObject());
    }

}
