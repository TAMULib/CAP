package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("/ir")
public class IRController {
	
	@Autowired
	IRRepo irRepo;
	
    @RequestMapping("/all")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse allIRs() {
        return new ApiResponse(SUCCESS, irRepo.findAll());
	}
    
    @RequestMapping("/create")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse createIRs(@RequestBody Map<String, String> data) {
    	irRepo.create(data.get("name"), data.get("uri"));
        return new ApiResponse(SUCCESS);
	}
    
    @RequestMapping("/update")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse updateIR(IR ir) {
    	irRepo.update(ir);
        return new ApiResponse(SUCCESS);
	}
    
    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse deleteIR(@PathVariable Long id) {
    	irRepo.delete(irRepo.read(id));
        return new ApiResponse(SUCCESS);
	}
    
    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
	public ApiResponse getIR(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, irRepo.read(id));
	}

}
