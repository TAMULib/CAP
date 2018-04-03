package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.ircontext.TransactionDetails;
import edu.tamu.cap.service.TransactingIRService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@RestController
@RequestMapping("ir-context/{type}/{irid}/transaction")
public class IRContextTransactionController {
    
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse startTransaction(TransactingIRService<?> irService) throws Exception {
        TransactionDetails transactionDetails = irService.startTransaction();
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }
    
    @RequestMapping(method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse refreshTransaction(TransactingIRService<?> irService, @Param("contextUri") String contextUri) throws Exception {
        
        Optional<TransactionDetails> transactionDetails = Optional.empty();
        
        Pattern pattern = Pattern.compile("tx:.*\\/?");
        System.out.println(pattern.pattern());
        System.out.println(contextUri);
        Matcher matcher = pattern.matcher(contextUri);
        if (matcher.find())
        {
            System.out.println(matcher.group(0));
            String rootUri = irService.getIR().getRootUri();
            rootUri += irService.getIR().getRootUri().endsWith("/") ? "" : "/"; 
            transactionDetails = Optional.ofNullable(irService.refreshTransaction(rootUri+matcher.group(0)));
            
        }
        
        ApiStatus status = transactionDetails.isPresent()?SUCCESS:ERROR;
        String message = transactionDetails.isPresent()?"Transaction successfully created":"Failed to find transaction token.";
        TransactionDetails td = transactionDetails.isPresent()?transactionDetails.get():null;
        
        if(transactionDetails.isPresent()) {
            return new ApiResponse(status, message, td);
        } else {
            return new ApiResponse(status, message);
        }
        
    }

}
