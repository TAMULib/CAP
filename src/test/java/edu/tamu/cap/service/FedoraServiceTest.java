package edu.tamu.cap.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.PostBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/example-context.xml")
@PrepareForTest(FedoraService.class)
public class FedoraServiceTest {
   
    private FedoraService fedoraService;
    
    private static final String IR_NAME = "Test IR";
    private static final String IR_ROOT_URI = "http://fcrepo.test";
    
    private static final String CONTEXT_URI_STRING = "http://fcrepo.test/context";
    private static final String VERSION_NAME = "Test Version";

    @Before
    public void setup() throws ClientProtocolException, IOException {
        fedoraService = new FedoraService();
        IR testIR = new IR();
        testIR.setName(IR_NAME);
        testIR.setRootUri(IR_ROOT_URI);
        fedoraService.setIr(testIR);
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void setIr() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void buildIRContext() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void verifyPing() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void verifyAuth() throws Exception {
//    }
//
//    @Test
//    public void verifyRoot() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void createContainer() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void getContainer() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void deleteContainer() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void createResource() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void resourceFixity() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void createMetadata() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void updateMetadata() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void deleteMetadata() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
    @Test
    public void createVersion() throws Exception {
        
        FcrepoClient FEDORA_CLIENT = fedoraService.buildClient();
        URI CONTEXT_URI = new URI(CONTEXT_URI_STRING);
        
        PostBuilder postBuilderSpy = Mockito.spy(new PostBuilder(CONTEXT_URI, FEDORA_CLIENT));
        doReturn(null).when(postBuilderSpy).perform();
        
        PowerMockito.whenNew(PostBuilder.class).withArguments(CONTEXT_URI,FEDORA_CLIENT).thenReturn(postBuilderSpy);
        
        IRContext contextWithNewVersion = fedoraService.createVersion(CONTEXT_URI_STRING, VERSION_NAME);
        
    }
//
//    @Test
//    public void getVersions() throws Exception {
//        
//    }
//
//    @Test
//    public void restoreVersion() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Test
//    public void deleteVersion() throws Exception {
//        // TODO Auto-generated method stub
//
//    }

}
