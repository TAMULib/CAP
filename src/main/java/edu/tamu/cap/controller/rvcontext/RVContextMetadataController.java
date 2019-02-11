package edu.tamu.cap.controller.rvcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RVService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("rv-context/{type}/{irid}/metadata")
public class RVContextMetadataController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createMetadata(RVService<?> rvService, @Param("contextUri") String contextUri, Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, rvService.createMetadata(contextUri, triple));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getMetadata(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, rvService.getMetadata(contextUri));
    }

    @RequestMapping(method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse updateMetadata(RVService<?> rvService, @Param("contextUri") String contextUri, Triple triple, @Param("newValue") String newValue) throws Exception {
        return new ApiResponse(SUCCESS, rvService.updateMetadata(contextUri, triple, newValue));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteMetadata(RVService<?> rvService, @Param("contextUri") String contextUri, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, rvService.deleteMetadata(contextUri, triple));
    }

}
