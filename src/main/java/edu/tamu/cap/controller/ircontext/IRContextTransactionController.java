package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.tamu.cap.model.ircontext.TransactionDetails;
import edu.tamu.cap.service.TransactingIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/transaction")
public class IRContextTransactionController {
    
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse startTransaction(HttpServletRequest request, HttpServletResponse response, TransactingIRService<?> irService) throws Exception {
        TransactionDetails transactionDetails = irService.startTransaction();
        
        ObjectNode cookieNode = objectMapper.createObjectNode();
        cookieNode.put("token", transactionDetails.getTransactionToken());
        cookieNode.put("expires", transactionDetails.getExpirationDateString());
        String cookieJson = objectMapper.writeValueAsString(cookieNode);
        
        Cookie cookie = new Cookie("transaction", URLEncoder.encode(cookieJson, "UTF-8"));
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(transactionDetails.getSecondsRemaining());
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }

}
