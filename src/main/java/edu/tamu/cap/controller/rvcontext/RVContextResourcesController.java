package edu.tamu.cap.controller.rvcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.service.FixityRVService;
import edu.tamu.cap.service.RVService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("rv-context/{type}/{rvid}/resource")
public class RVContextResourcesController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(RVService<?> rvService, @Param("contextUri") String contextUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, rvService.createResource(contextUri, file));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getResources(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, rvService.getResource(contextUri));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteResources(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        rvService.deleteResource(contextUri);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/fixity", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse checkFixity(FixityRVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, rvService.resourceFixity(contextUri));
    }

}
