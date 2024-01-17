package com.sanmartindev.clockinoutbackend.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clock In Out API - RESTful API with Spring Boot 3.2.1")
                        .version("1.0.0")
                        .description("Clock In Out API Documentation")
                        .termsOfService("")
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org")));
    }

}
