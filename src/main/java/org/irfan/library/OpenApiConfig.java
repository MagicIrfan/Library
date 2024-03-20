package org.irfan.library;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String BEARER_AUTH = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Library API")
                        .version("v1")
                        .description("API REST de gestion de livres"))
                .components(new Components().addSecuritySchemes(BEARER_AUTH,
                        new SecurityScheme().name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}