package edu.tamu.cap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import edu.tamu.weaver.email.config.WeaverEmailConfig;
import edu.tamu.weaver.email.service.EmailSender;
import edu.tamu.weaver.email.service.WeaverEmailService;

@Configuration
@Profile("!test")
public class AppEmailConfig extends WeaverEmailConfig {
    @Value("${app.email.host}")
    String emailHost;

    @Bean
    @Override
    public EmailSender emailSender() {
        WeaverEmailService emailService = new WeaverEmailService();
        emailService.setHost(emailHost);
        return emailService;
    }

}
