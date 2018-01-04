package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.ir.IRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-proxy/{type}/{irid}")
public class IRProxyController {

	@RequestMapping(value = "/container", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse createContainer(IRService irService, String contextUri, String name) throws Exception {
		irService.createContainer(contextUri, name);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/containers")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getContainers(IRService irService, String contextUri) throws Exception {
		return new ApiResponse(SUCCESS, irService.getContainers(contextUri));
	}

	@RequestMapping(value = "/containers/delete", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse deleteContainers(IRService irService, List<String> containerUris) throws Exception {
		containerUris.forEach(uri -> {
			try {
				irService.deleteContainer(uri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/metadata")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse getProperties(IRService irService, String contextUri) throws Exception {
		return new ApiResponse(SUCCESS, irService.getMetadata());
	}

	@RequestMapping(value = "/test/ping", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse testIRPing(IRService irService) throws Exception {
		irService.verifyPing();
		return new ApiResponse(SUCCESS, "Ping was successful!");
	}

	@RequestMapping(value = "/test/auth", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse testIRAuth(IRService irService) throws Exception {
		irService.verifyAuth();
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/test/content", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ApiResponse testIRContent(IRService irService) throws Exception {
		irService.verifyRoot();
		return new ApiResponse(SUCCESS);
	}

}
