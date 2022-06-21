package edu.tamu.cap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "edu.tamu.*", excludeFilters = { @Filter(type = FilterType.REGEX, pattern = "edu.tamu.weaver.wro.model.*"), @Filter(type = FilterType.REGEX, pattern = "edu.tamu.weaver.wro.service.*") })
public class CapApplication extends SpringBootServletInitializer {

    /**
     * Entry point to the application from within servlet.
     *
     * @param args String[]
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class, args);
    }

    /**
     * Entry point to the application if run using spring-boot:run.
     *
     * @param application SpringApplicationBuilder
     *
     * @return SpringApplicationBuilder
     *
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapApplication.class);
    }

}
