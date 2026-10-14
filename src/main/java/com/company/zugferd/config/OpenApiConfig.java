package com.company.zugferd.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI zugferdOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ZUGFeRD Invoice API")
                        .description("Generates CII-compliant ZUGFeRD PDF/A-3 invoices for Endur integration. "
                                + "Versioned under /api/v1. Requires scope zugferd.generate.")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enterprise JWT with scope zugferd.generate")));
    }
}
