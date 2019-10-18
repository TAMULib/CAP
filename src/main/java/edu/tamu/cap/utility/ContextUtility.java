package edu.tamu.cap.utility;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ContextUtility {

    private static final Pattern TRANSACTION_PATTERN = Pattern.compile("tx:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");

    public static String buildFullContextURI(String rootUri, String contextUri) {
        if (!contextUri.startsWith(rootUri)) {
            if (rootUri.endsWith("/")) {
                rootUri = StringUtils.chop(rootUri);
            }
            if (contextUri.startsWith("/")) {
                contextUri = contextUri.substring(1);
            }
            contextUri = String.format("%s/%s", rootUri, contextUri);
        }
        return contextUri;
    }

    public static Optional<String> getTransactionToken(String contextUri) {
        Matcher transactionMatcher = TRANSACTION_PATTERN.matcher(contextUri);
        if (transactionMatcher.find()) {
            String tid = transactionMatcher.group(0);
            return Optional.of(tid);
        }
        return Optional.empty();
    }

}
