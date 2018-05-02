package edu.tamu.cap.mock.fedora.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.tamu.cap.mock.fedora.loader.MockFedoraLoader;

@Profile("test")
@RestController
@RequestMapping("/mock/fcrepo/rest")
public class MockFedoraController {

    @Autowired
    private MockFedoraLoader mockFedoraLoader;

    @RequestMapping(value = "/**/*", headers = "Accept=application/rdf+xml", method = RequestMethod.GET, produces = "application/rdf+xml")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getContainer() throws JsonProcessingException, IOException {
        return mockFedoraLoader.getContainer();
    }

    @RequestMapping(value = "/**/*/fcr:versions", headers = "Accept=application/rdf+xml", method = RequestMethod.GET, produces = "application/rdf+xml")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getVersions() throws JsonProcessingException, IOException {
        return mockFedoraLoader.getVersions();
    }

    @RequestMapping(value = "/**/*/fcr:versions", headers = { "Slug=TestVersion1", "Slug=TestVersion2" }, method = RequestMethod.POST, produces = "text/plain")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String createVersion() throws JsonProcessingException, IOException {
        return "Status: 204 No Content";
    }

}
