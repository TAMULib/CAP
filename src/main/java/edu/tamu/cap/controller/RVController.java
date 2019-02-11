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

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.repo.RVRepo;
import edu.tamu.cap.service.RVType;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/rv")
public class RVController {

	@Autowired
	private RVRepo rvRepo;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createRV(@RequestBody @WeaverValidatedModel RV rv) {
        logger.info("Creating RV:  " + rv.getName() + " with schema " + rv.getSchemas());
        return new ApiResponse(SUCCESS, rvRepo.create(rv));
    }
	
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse allRVs() {
		return new ApiResponse(SUCCESS, rvRepo.findAll());
	}

	@RequestMapping(method = RequestMethod.PUT)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
	public ApiResponse updateRV(@RequestBody @WeaverValidatedModel RV rv) {
		logger.info("Updating RV:  " + rv.getName());
		return new ApiResponse(SUCCESS, rvRepo.update(rv));
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteRV(@RequestBody @WeaverValidatedModel RV rv) {
		logger.info("Deleating RV:  " + rv.getName());
		rvRepo.delete(rv);
		return new ApiResponse(SUCCESS);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getRV(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, rvRepo.read(id));
    }

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getRVTypes() {
		return new ApiResponse(SUCCESS, RVType.getValues());
	}
	
}
