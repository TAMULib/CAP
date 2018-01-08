package edu.tamu.cap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "edu.tamu.*", "wro.*" })
public class CapApplication extends SpringBootServletInitializer {

    /**
     * Entry point to the application from within servlet.
     *
     * @param args
     *            String[]
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class, args);
    }

    /**
     * Entry point to the application if run using spring-boot:run.
     *
     * @param application
     *            SpringApplicationBuilder
     *
     * @return SpringApplicationBuilder
     *
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapApplication.class);
    }

}
