package edu.tamu.cap.mock.fedora.loader;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Profile("test")
public class MockFedoraLoader {

    @Value("classpath:mock/container.xml")
    private Resource container;

    @Value("classpath:mock/versions.xml")
    private Resource versions;

    public String getContainer() throws JsonProcessingException, IOException {
        return StreamUtils.copyToString(container.getInputStream(), Charset.defaultCharset());
    }

    public String getVersions() throws JsonProcessingException, IOException {
        return StreamUtils.copyToString(versions.getInputStream(), Charset.defaultCharset());
    }

}
