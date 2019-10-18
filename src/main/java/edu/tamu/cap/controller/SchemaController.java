package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.exceptions.OntModelReadException;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.SchemaRepo;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/schema")
public class SchemaController {

    @Autowired
    private SchemaRepo schemaRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createSchema(@RequestBody @WeaverValidatedModel Schema schema) {
        logger.info("Creating schema:  " + schema.getName());
        return new ApiResponse(SUCCESS, schemaRepo.create(schema));
    }

    @GetMapping
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse allSchemas() {
        return new ApiResponse(SUCCESS, schemaRepo.findAll());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
    public ApiResponse updateSchema(@RequestBody @WeaverValidatedModel Schema schema) {
        logger.info("Updating schema:  " + schema.getName());
        return new ApiResponse(SUCCESS, schemaRepo.update(schema));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE, joins = { RepositoryView.class }, path = { "schemas" }) })
    public ApiResponse deleteSchema(@RequestBody @WeaverValidatedModel Schema schema) {
        logger.info("Deleating schema:  " + schema.getName());
        schemaRepo.delete(schema);
        return new ApiResponse(SUCCESS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse getSchema(@PathVariable("id") Long id) {
        return new ApiResponse(SUCCESS, schemaRepo.read(id));
    }

    @GetMapping("/properties")
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse propertiesByNamespace(@RequestParam("namespace") String namespace) throws OntModelReadException {
        return new ApiResponse(SUCCESS, schemaRepo.findPropertiesByNamespace(namespace));
    }

}
