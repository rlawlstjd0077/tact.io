package com.tact.io.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.*;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * {url}/swagger-ui/index.html
 */
@EnableOpenApi
@Configuration
public class SwaggerConfig {
    @Bean
    Docket mainApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("main-api")
                .select()
                .paths(regex(""))
                .build();
    }
}
