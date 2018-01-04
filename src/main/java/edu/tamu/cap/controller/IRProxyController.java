package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Arrays;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.service.ir.IRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-proxy/{type}/{irid}")
public class IRProxyController {

	@RequestMapping(value = "/container", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse createContainer(IRService irService, @Param("contextUri") String contextUri, @PayloadArgName("name") String name) throws Exception {
		irService.createContainer(contextUri, name);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/containers", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getContainers(IRService irService, @Param("contextUri") String contextUri) throws Exception {
		return new ApiResponse(SUCCESS, irService.getContainers(contextUri));
	}

	@RequestMapping(value = "/container/delete", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse deleteContainer(IRService irService, String containerUri) throws Exception {
		try {
			irService.deleteContainer(containerUri);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/metadata", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getProperties(IRService irService, @Param("contextUri") String contextUri) throws Exception {
		return new ApiResponse(SUCCESS, irService.getMetadata(contextUri));
	}

}
