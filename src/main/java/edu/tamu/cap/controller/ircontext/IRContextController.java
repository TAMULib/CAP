package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.service.IRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}")
public class IRContextController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse get(IRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.getIRContext(contextUri));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse delete(IRService<?> irService, @Param("containerUri") String containerUri) throws Exception {
        try {
            irService.deleteIRContext(containerUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse(SUCCESS);
    }
    
    @RequestMapping(value="/triples", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getTriples(IRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.getTriples(irService, contextUri));
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
        irService.deleteVersion(versionUri);
        return new ApiResponse(SUCCESS);
    }

}
