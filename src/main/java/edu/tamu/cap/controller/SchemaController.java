package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping("/all")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse allSchemas() {
		return new ApiResponse(SUCCESS, schemaRepo.findAll());
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
	public ApiResponse createSchema(@RequestBody @WeaverValidatedModel Schema schema) {
		logger.info("Creating schema:  " + schema.getName());
		return new ApiResponse(SUCCESS, schemaRepo.create(schema));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
	public ApiResponse updateSchema(@RequestBody @WeaverValidatedModel Schema schema) {
		logger.info("Updating schema:  " + schema.getName());
		return new ApiResponse(SUCCESS, schemaRepo.update(schema));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteSchema(@RequestBody @WeaverValidatedModel Schema schema) {
		logger.info("Deleating schema:  " + schema.getName());
		schemaRepo.delete(schema);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getSchema(@PathVariable Long id) {
		return new ApiResponse(SUCCESS, schemaRepo.read(id));
	}

}
