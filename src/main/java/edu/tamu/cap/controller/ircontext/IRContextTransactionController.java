package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ApiResponse makeQuery(HttpServletRequest request, HttpServletResponse response, TransactingIRService<?> irService) throws Exception {
        Map<String,String> transactionDetails = irService.startTransaction();

        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss z");
        ZonedDateTime cookieExpires = ZonedDateTime.parse(transactionDetails.get("expires"),f);
        int cookieMaxAge = (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")),cookieExpires);

        Cookie cookie = new Cookie("transaction", transactionDetails.get("url"));
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(cookieMaxAge);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }

}
