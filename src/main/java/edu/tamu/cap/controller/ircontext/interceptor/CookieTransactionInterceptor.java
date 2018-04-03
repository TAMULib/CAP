package edu.tamu.cap.controller.ircontext.interceptor;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.tamu.weaver.context.SpringContext;

public class CookieTransactionInterceptor extends HandlerInterceptorAdapter {
    
    private static List<String> TRANSACTION_METHODS = Arrays.asList(new String[] { "POST", "PUT", "DELETE" });
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {

    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
//        ObjectMapper objectMapper = SpringContext.bean("objectMapper");
//        
//        String method = request.getMethod();
//        
//        System.out.println("INTERCEPTED!");
//        
//        if (response.getStatus() > 199 && response.getStatus() < 300 && TRANSACTION_METHODS.contains(method)) {
//            
//            System.out.println(response.getStatus());
//            
//            Cookie[] cookies = request.getCookies(); 
//            
//            if(cookies != null)
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals("transaction")) {
//                    
//                    JsonNode cookieObject = objectMapper.readTree(URLDecoder.decode(cookie.getValue(), "UTF-8"));
//                    
//                    ObjectNode cookieNode = objectMapper.createObjectNode();
//                    
//                    cookieNode.put("transactionToken", cookieObject.get("transactionToken").asText());
//                    cookieNode.put("secondsRemaining", 180);
//                    String cookieJson = objectMapper.writeValueAsString(cookieNode);
//                    Cookie c = new Cookie("transaction", URLEncoder.encode(cookieJson, "UTF-8"));
//                    c.setMaxAge(180);
//                    
//                    response.addCookie(c);
//                    break;
//                }
//            }
//        }
        return true;
    }
}
