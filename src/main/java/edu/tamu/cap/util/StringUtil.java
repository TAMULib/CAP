package edu.tamu.cap.util;

import org.apache.jena.ext.com.google.common.collect.ImmutableMap;

public class StringUtil {

    /**
     * See http://www.w3.org/TR/rdf-sparql-query/#grammarEscapes
     * See http://www.onemusicapi.com/blog/2014/10/08/escaping-sparql-java/index.html
     * @param name
     * @return
     */
    private static final ImmutableMap<Object, Object> SPARQL_ESCAPE_SEARCH_REPLACEMENTS = ImmutableMap.builder()
        .put("\t", "\\t")
        .put("\n", "\\n")
        .put("\r", "\\r")
        .put("\b", "\\b")
        .put("\f", "\\f")
        .put("\"", "\\\"")
        .put("'", "\\'")
        .put("\\", "\\\\")
        .build();

    public static String escape(String string) {

        StringBuffer bufOutput = new StringBuffer(string);
        for (int i = 0; i < bufOutput.length(); i++) {
            String replacement = (String) SPARQL_ESCAPE_SEARCH_REPLACEMENTS.get("" + bufOutput.charAt(i));
            if(replacement!=null) {
                bufOutput.deleteCharAt(i);
                bufOutput.insert(i, replacement);
                // advance past the replacement
                i += (replacement.length() - 1);
            }
        }
        return bufOutput.toString();
    }

    public static String removeQuotes(String string) {

        if((string.startsWith("'") && string.endsWith("'")) || (string.startsWith("\"") && string.endsWith("\""))) {
            string = string.substring(1);
            string = string.substring(0, string.length() - 1);
        }
        return string;

    }

}
