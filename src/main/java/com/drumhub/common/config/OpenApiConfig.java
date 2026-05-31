package com.drumhub.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI drumhubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DrumHub API")
                        .version("v1")
                        .description("DrumHub backend API — groove sharing and beat editor platform"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT bearer token — provide the token obtained from /api/auth/login")));
        // NOTE: bearerAuth is registered in components but NOT applied globally yet.
        // Step 1 (JWT slice) will add @SecurityRequirement annotations per endpoint
        // or apply it globally via a security requirement on the OpenAPI object.
    }
}
