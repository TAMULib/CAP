package edu.tamu.cap.controller.rvcontext;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.rvcontext.TransactionDetails;
import edu.tamu.cap.service.TransactingRVService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@RestController
@RequestMapping("rv-context/{type}/{rvid}/transaction")
public class RVContextTransactionController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse startTransaction(TransactingRVService<?> rvService) throws Exception {
        TransactionDetails transactionDetails = rvService.startTransaction();
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }

    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse commitTransaction(TransactingRVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {

        Optional<String> trancationToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        String rootUri = rvService.getRV().getRootUri();

        ApiStatus status = trancationToken.isPresent()?SUCCESS:ERROR;
        String message = trancationToken.isPresent()?"Transaction successfully commited":"Failed to find transaction token.";

        rvService.commitTransaction(rootUri+trancationToken.get());

        return new ApiResponse(status, message);
    }

    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse rollbackTransaction(TransactingRVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {

        Optional<String> trancationToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        String rootUri = rvService.getRV().getRootUri();

        ApiStatus status = trancationToken.isPresent()?SUCCESS:ERROR;
        String message = trancationToken.isPresent()?"Transaction successfully created":"Failed to find transaction token.";

        rvService.rollbackTransaction(rootUri+trancationToken.get());

        return new ApiResponse(status, message);
    }

    @RequestMapping(method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse refreshTransaction(TransactingRVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {

        Optional<TransactionDetails> transactionDetails = Optional.empty();
        Optional<String> transactionToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        if (transactionToken.isPresent()) {
            String rootUri = rvService.getRV().getRootUri();
            rootUri += rvService.getRV().getRootUri().endsWith("/") ? "" : "/";
            transactionDetails = Optional.ofNullable(rvService.refreshTransaction(rootUri+transactionToken.get()));
        }

        boolean transactionDetailsPresent = transactionDetails.isPresent();

        ApiStatus status = transactionDetailsPresent?SUCCESS:ERROR;
        String message = transactionDetailsPresent?"Transaction successfully created":"Failed to find transaction token.";
        TransactionDetails td = transactionDetailsPresent?transactionDetails.get():null;

        if(transactionDetailsPresent) {
            return new ApiResponse(status, message, td);
        } else {
            return new ApiResponse(status, message);
        }

    }

    private String extractTokenFromContextUri(String contextUri) {
        String transactionToken = null;

        Pattern pattern = Pattern.compile("tx:.*\\/?");
        Matcher matcher = pattern.matcher(contextUri);
        if (matcher.find()) {
            transactionToken = matcher.group(0);
        }

        return transactionToken;

    }

}
