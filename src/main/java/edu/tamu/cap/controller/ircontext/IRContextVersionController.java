package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.service.VersioningIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/version")
public class IRContextVersionController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createVersion(VersioningIRService<?> irService, @Param("contextUri") String contextUri, @PayloadArgName("name") String name) throws Exception {
        return new ApiResponse(SUCCESS, irService.createVersion(contextUri, name));
    }
    
    @RequestMapping(method = PATCH)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse restoreVersion(VersioningIRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.restoreVersion(contextUri));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteVersion(VersioningIRService<?> irService, @Param("contextUri") String versionUri) throws Exception {
        irService.deleteVersion(versionUri);
        return new ApiResponse(SUCCESS);
    }

}
