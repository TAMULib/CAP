package edu.tamu.cap.controller;

import static edu.tamu.cap.service.IRType.FEDORA;
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

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.weaver.response.ApiResponse;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public final class IRControllerTest {

    @InjectMocks
    private IRController iRController;

    @Mock
    private IRRepo irRepo;

    private IR fedoraIR;

    @BeforeEach
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);

        fedoraIR = getMockFedoraIR();
    }

    @Test
    public void testCreateIR() {
        IR responseIR = null;

        when(irRepo.create(any(IR.class))).thenReturn(fedoraIR);

        ApiResponse response = iRController.createIR(fedoraIR);
        Collection<Object> payload = response.getPayload().values();
        if (payload.size() == 1) {
            responseIR = (IR) payload.toArray()[0];
        }

        assertEquals(fedoraIR.getId(), responseIR.getId(), "Fedora IR has incorrect ID!");
        assertEquals(fedoraIR.getName(), responseIR.getName(), "Fedora IR has incorrect Name!");
        assertEquals(fedoraIR.getRootUri(), responseIR.getRootUri(), "Fedora IR has incorrect Root URI!");
        assertEquals(fedoraIR.getSchemas(), responseIR.getSchemas(), "Fedora IR has incorrect Schemas!");
        assertEquals(fedoraIR.getType(), responseIR.getType(), "Fedora IR has incorrect Type!");
        assertEquals(fedoraIR.getUsername(), responseIR.getUsername(), "Fedora IR has incorrect Username!");
        assertEquals(fedoraIR.getPassword(), responseIR.getPassword(), "Fedora IR has incorrect Password!");
    }

    private IR getMockFedoraIR() {
        // TODO: convert this into json file to be imported.
        List<Schema> schemas = new ArrayList<Schema>();
        Schema schema = new Schema();
        schema.setId(1L);
        schema.setName("Schema Name");
        schema.setNamespace("Schema Namespace");
        schema.setAbbreviation("Schema Abbreviation");
        schemas.add(schema);

        IR ir = new IR();
        ir.setId(123456789L);
        ir.setName("IR Name");
        ir.setRootUri("http://localhost/fedora");
        ir.setSchemas(schemas);
        ir.setType(FEDORA);
        ir.setUsername("");
        ir.setPassword("");
        return ir;
    }

}
