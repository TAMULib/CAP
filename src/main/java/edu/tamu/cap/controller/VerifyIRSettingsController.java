package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.VerifyingIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir/{type}/verify")
public class VerifyIRSettingsController {

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyIRPing(VerifyingIRService<?> irService) throws Exception {
        irService.verifyPing();
        return new ApiResponse(SUCCESS, "Ping was successful!");
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyIRAuth(VerifyingIRService<?> irService) throws Exception {
        irService.verifyAuth();
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/content", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyIRContent(VerifyingIRService<?> irService) throws Exception {
        irService.verifyRoot();
        return new ApiResponse(SUCCESS);
    }

}
