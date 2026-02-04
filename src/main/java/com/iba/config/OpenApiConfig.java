package com.iba.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ĩbá API")
                        .version("1.0.0")
                        .description("API para Sistema de Monitoramento Ambiental Comunitário")
                        .contact(new Contact()
                                .name("Ĩbá Team")
                                .email("contact@iba.com")));
    }
}
