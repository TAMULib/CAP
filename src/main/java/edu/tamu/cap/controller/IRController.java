package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/ir")
public class IRController {
	
	@Autowired
	IRRepo irRepo;
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping("/all")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse allIRs() {
        return new ApiResponse(SUCCESS, irRepo.findAll());
	}
    
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
	public ApiResponse createIRs(@RequestBody @WeaverValidatedModel IR ir) {
        logger.info("Creating IR:  " + ir.getName());
        return new ApiResponse(SUCCESS, irRepo.create(ir));
	}
    
    @RequestMapping("/update")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse updateIR(IR ir) {
    	irRepo.update(ir);
        return new ApiResponse(SUCCESS);
	}
    
    @RequestMapping(value="/delete", method=RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteIR(@RequestBody @WeaverValidatedModel IR ir) {
        logger.info("Deleating IR:  " + ir.getName());
    	irRepo.delete(ir);
        return new ApiResponse(SUCCESS);
	}
    
    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse getIR(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, irRepo.read(id));
	}

}
