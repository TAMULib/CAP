package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.IRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/resource")
public class IRContextResourcesController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(IRService<?> irService, @Param("resourceUri") String resourceUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, irService.createResource(resourceUri, file));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getResources(IRService<?> irService, @Param("resourceUri") String resourceUri) throws Exception {
        return new ApiResponse(SUCCESS, irService.getResource(resourceUri));
    }
    
    @RequestMapping(method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse updateResources(IRService<?> irService, @Param("resourceUri") String resourceUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, irService.updateResource(resourceUri, file));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteResources(IRService<?> irService, @Param("resourceUri") String resourceUri) throws Exception {
        System.out.println("\n\n"+resourceUri+"\n\n");
        irService.deleteResource(resourceUri);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/fixity", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(IRService<?> irService, @RequestParam Map<String, String> tripleMap) throws Exception {
        return new ApiResponse(SUCCESS, irService.resourceFixity(Triple.of(tripleMap)));
    }

}
