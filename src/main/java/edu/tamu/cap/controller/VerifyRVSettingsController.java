package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.VerifyingRVService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("rv/{type}/verify")
public class VerifyRVSettingsController {

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyRVPing(VerifyingRVService<?> rvService) throws Exception {
        rvService.verifyPing();
        return new ApiResponse(SUCCESS, "Ping was successful!");
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyRVAuth(VerifyingRVService<?> rvService) throws Exception {
        rvService.verifyAuth();
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/content", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyIRContent(VerifyingRVService<?> rvService) throws Exception {
        rvService.verifyRoot();
        return new ApiResponse(SUCCESS);
    }

}
