package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.TransactionDetails;
import edu.tamu.cap.service.TransactingRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/transaction")
public class RepositoryViewContextTransactionController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse startTransaction(TransactingRepositoryViewService<?> repositoryViewService) throws Exception {
        TransactionDetails transactionDetails = repositoryViewService.startTransaction();
        return new ApiResponse(SUCCESS, "Transaction successfully created", transactionDetails);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse commitTransaction(TransactingRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        Optional<String> trancationToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        String rootUri = repositoryViewService.getRepositoryView().getRootUri();

        ApiStatus status = trancationToken.isPresent() ? SUCCESS : ERROR;
        String message = trancationToken.isPresent() ? "Transaction successfully commited" : "Failed to find transaction token.";

        repositoryViewService.commitTransaction(rootUri + trancationToken.get());

        return new ApiResponse(status, message);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse rollbackTransaction(TransactingRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        Optional<String> trancationToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        String rootUri = repositoryViewService.getRepositoryView().getRootUri();

        ApiStatus status = trancationToken.isPresent() ? SUCCESS : ERROR;
        String message = trancationToken.isPresent() ? "Transaction successfully created" : "Failed to find transaction token.";

        repositoryViewService.rollbackTransaction(rootUri + trancationToken.get());

        return new ApiResponse(status, message);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse refreshTransaction(TransactingRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        Optional<TransactionDetails> transactionDetails = Optional.empty();
        Optional<String> transactionToken = Optional.ofNullable(extractTokenFromContextUri(contextUri));

        if (transactionToken.isPresent()) {
            String rootUri = repositoryViewService.getRepositoryView().getRootUri();
            rootUri += repositoryViewService.getRepositoryView().getRootUri().endsWith("/") ? "" : "/";
            transactionDetails = Optional.ofNullable(repositoryViewService.refreshTransaction(rootUri + transactionToken.get()));
        }

        boolean transactionDetailsPresent = transactionDetails.isPresent();

        ApiStatus status = transactionDetailsPresent ? SUCCESS : ERROR;
        String message = transactionDetailsPresent ? "Transaction successfully created" : "Failed to find transaction token.";
        TransactionDetails td = transactionDetailsPresent ? transactionDetails.get() : null;

        if (transactionDetailsPresent) {
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
