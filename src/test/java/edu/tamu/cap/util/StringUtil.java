package edu.tamu.cap.util;

public class StringUtil {

    public static String removeQuotes(String string) {
        
        if(string.startsWith("'")||string.startsWith("\"")) {
            string = string.substring(1);
        } 
        
        if(string.endsWith("'")||string.endsWith("\"")) {
            string = string.substring(0, string.length() - 1);
        }
        
        return string;
    }
    
}
