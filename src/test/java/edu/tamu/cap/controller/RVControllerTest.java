package edu.tamu.cap.controller;

import static edu.tamu.cap.service.RVType.FEDORA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.RVRepo;
import edu.tamu.weaver.response.ApiResponse;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public final class RVControllerTest {

    @InjectMocks
    private RVController rVController;

    @Mock
    private RVRepo iVRepo;

    private RV fedoraRV;

    @BeforeEach
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);

        fedoraRV = getMockFedoraRV();
    }

    @Test
    public void testCreateRV() {
        RV responseRV = null;

        when(iVRepo.create(any(RV.class))).thenReturn(fedoraRV);

        ApiResponse response = rVController.createRV(fedoraRV);
        Collection<Object> payload = response.getPayload().values();
        if (payload.size() == 1) {
            responseRV = (RV) payload.toArray()[0];
        }

        assertEquals(fedoraRV.getId(), responseRV.getId(), "Fedora RV has incorrect ID!");
        assertEquals(fedoraRV.getName(), responseRV.getName(), "Fedora IR has incorrect Name!");
        assertEquals(fedoraRV.getRootUri(), responseRV.getRootUri(), "Fedora IR has incorrect Root URI!");
        assertEquals(fedoraRV.getSchemas(), responseRV.getSchemas(), "Fedora IR has incorrect Schemas!");
        assertEquals(fedoraRV.getType(), responseRV.getType(), "Fedora IR has incorrect Type!");
        assertEquals(fedoraRV.getUsername(), responseRV.getUsername(), "Fedora IR has incorrect Username!");
        assertEquals(fedoraRV.getPassword(), responseRV.getPassword(), "Fedora IR has incorrect Password!");
    }

    private RV getMockFedoraRV() {
        // TODO: convert this into json file to be imported.
        List<Schema> schemas = new ArrayList<Schema>();
        Schema schema = new Schema();
        schema.setId(1L);
        schema.setName("Schema Name");
        schema.setNamespace("Schema Namespace");
        schema.setAbbreviation("Schema Abbreviation");
        schemas.add(schema);

        RV rv = new RV();
        rv.setId(123456789L);
        rv.setName("RV Name");
        rv.setRootUri("http://localhost/fedora");
        rv.setSchemas(schemas);
        rv.setType(FEDORA);
        rv.setUsername("");
        rv.setPassword("");
        return rv;
    }

}
