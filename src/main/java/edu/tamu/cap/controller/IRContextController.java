package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.IRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}")
public class IRContextController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse get(IRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.getContainer(contextUri));
    }

    @RequestMapping(value = "/container", method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse create(IRService<?> irService, @Param("contextUri") String contextUri, @PayloadArgName("name") String name) throws Exception {
        return new ApiResponse(SUCCESS, irService.createContainer(contextUri, name));
    }

    @RequestMapping(value = "/container", method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse delete(IRService<?> irService, @Param("containerUri") String containerUri) throws Exception {
        try {
            irService.deleteContainer(containerUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/resource", method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(IRService<?> irService, @Param("contextUri") String contextUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, irService.createResource(contextUri, file));
    }

    @RequestMapping(value = "/resource/fixity", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(IRService<?> irService, @RequestParam Map<String, String> tripleMap) throws Exception {
        return new ApiResponse(SUCCESS, irService.resourceFixity(Triple.of(tripleMap)));
    }

    @RequestMapping(value = "/metadata", method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createMetadata(IRService<?> irService, Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, irService.createMetadata(triple));
    }

    @RequestMapping(value = "/metadata", method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteMetadata(IRService<?> irService, @RequestParam Map<String, String> tripleMap) throws Exception {
        return new ApiResponse(SUCCESS, irService.deleteMetadata(Triple.of(tripleMap)));
    }

    @RequestMapping(value = "/metadata", method = PATCH)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse updateMetadata(IRService<?> irService, @Param("contextUri") String contextUri, String spqarl) throws Exception {
        return new ApiResponse(SUCCESS, irService.updateMetadata(contextUri, spqarl));
    }
    
    @RequestMapping(value = "/version", method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createVersion(IRService<?> irService, @Param("contextUri") String contextUri, @PayloadArgName("name") String name) throws Exception {
        return new ApiResponse(SUCCESS, irService.createVersion(contextUri, name));
    }
    
    @RequestMapping(value = "/version", method = PATCH)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse restoreVersion(IRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.restoreVersion(contextUri));
    }
    
    @RequestMapping(value = "/version", method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteVersion(IRService<?> irService, @Param("versionUri") String versionUri) throws Exception {
        System.out.println("\n\n"+versionUri+"\n\n");
        irService.deleteVersion(versionUri);
        return new ApiResponse(SUCCESS);
    }

}
