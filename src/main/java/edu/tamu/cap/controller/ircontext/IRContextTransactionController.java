package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.TransactingIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/transaction")
public class IRContextTransactionController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse makeQuery(TransactingIRService<?> irService) throws Exception {
        System.out.println("Controller");
        return new ApiResponse(SUCCESS, "Transaction successfully created", irService.startTransaction());
    }
    
}
