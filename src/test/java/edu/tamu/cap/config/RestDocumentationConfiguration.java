package edu.tamu.cap.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;


import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

@TestConfiguration
public class RestDocumentationConfiguration implements RestDocsMockMvcConfigurationCustomizer {
  
  @Override
  public void customize(MockMvcRestDocumentationConfigurer configurer) {
    
    
  }

  @Bean
  public RestDocumentationResultHandler restDocumentation() {
      return MockMvcRestDocumentation.document("{method-name}", 
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()));
  }

}
