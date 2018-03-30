package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.ircontext.TransactionDetails;
import edu.tamu.cap.service.TransactingIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/transaction")
public class IRContextTransactionController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse makeQuery(HttpServletRequest request, HttpServletResponse response, TransactingIRService<?> irService) throws Exception {
        TransactionDetails transactionDetails = irService.startTransaction();
        System.out.println("\n\n\n token: "+transactionDetails.getTransactionToken());
        System.out.println("expires: "+transactionDetails.getExpirationDate());
        System.out.println("token: "+transactionDetails.getSecondsRemaining());        
        Cookie cookie = new Cookie("transaction", URLEncoder.encode("{\"token\":\""+transactionDetails.getTransactionToken()+"\",\"expires\":\""+transactionDetails.getExpirationDateString()+"\"}"));
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(transactionDetails.getSecondsRemaining());
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }

}
