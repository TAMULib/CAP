package edu.tamu.cap.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import edu.tamu.weaver.messaging.annotation.WeaverMessageListener;

@Service
public class MessageListenerService {

    @WeaverMessageListener(destination = "cap")
    private void printMessage(Map<String, String> content) {
        System.out.println("\n\n\ncontextPath: " + content.get("contextPath") + "\nrepositoryType: " + content.get("repositoryType") + "\naction: " + content.get("action") + "\n\n\n");
    }
}
