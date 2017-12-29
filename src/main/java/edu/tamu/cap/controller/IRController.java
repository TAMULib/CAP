package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.controller.aspect.InjectIRService;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.cap.service.ir.IRService;
import edu.tamu.cap.service.ir.IRType;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/ir")
public class IRController {

	@Autowired
	private IRRepo irRepo;
	
	@Autowired
    private ObjectMapper objectMapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/all")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse allIRs() {
		return new ApiResponse(SUCCESS, irRepo.findAll());
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
	public ApiResponse createIRs(@RequestBody @WeaverValidatedModel IR ir) {
		logger.info("Creating IR:  " + ir.getName());
		return new ApiResponse(SUCCESS, irRepo.create(ir));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
	public ApiResponse updateIR(@RequestBody @WeaverValidatedModel IR ir) {
		logger.info("Updating IR:  " + ir.getName());
		return new ApiResponse(SUCCESS, irRepo.update(ir));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteIR(@RequestBody @WeaverValidatedModel IR ir) {
		logger.info("Deleating IR:  " + ir.getName());
		irRepo.delete(ir);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse testIRPing() {
		return new ApiResponse(SUCCESS, IRType.getValues());
	}

	@RequestMapping(value = "/container", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse createContainer(IRService irService, @RequestBody Map<String, Object> data) throws Exception {
		
		IR ir = objectMapper.convertValue(data.get("ir"), IR.class);
		String name = objectMapper.convertValue(data.get("name"), String.class);
		irService.createContainer(ir, name);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/containers", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse getContainers(@RequestBody @WeaverValidatedModel IR ir, IRService irService) throws Exception {
		return new ApiResponse(SUCCESS, irService.getContainers(ir));
	}
	
	@RequestMapping(value = "/containers/delete", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse deleteContainers(IRService irService, @RequestBody Map<String, Object> data) throws Exception {
				
		IR ir = objectMapper.convertValue(data.get("ir"), IR.class);
        
		List<String> containerUris = (List<String>) data.get("containerUris");
		
        containerUris.forEach(uri->{
			try {
				irService.deleteContainer(ir, uri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/test/ping", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse testIRPing(@RequestBody @WeaverValidatedModel IR ir, IRService irService) throws Exception {
		irService.verifyPing(ir);
		return new ApiResponse(SUCCESS, "Ping was successful!");
	}

	@RequestMapping(value = "/test/auth", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse testIRAuth(@RequestBody @WeaverValidatedModel IR ir, IRService irService) throws Exception {
		irService.verifyAuth(ir);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/test/content", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@InjectIRService
	public ApiResponse testIRContent(@RequestBody @WeaverValidatedModel IR ir, IRService irService) throws Exception {
		irService.verifyRoot(ir);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getIR(@PathVariable Long id) {
		return new ApiResponse(SUCCESS, irRepo.read(id));
	}

}
