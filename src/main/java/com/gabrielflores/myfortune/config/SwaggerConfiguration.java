package com.gabrielflores.myfortune.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Configuration
@OpenAPIDefinition
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final Environment environment;

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title(environment.getProperty("api.title"))
                        .version(environment.getProperty("api.version"))
                        .description(environment.getProperty("api.description"))
                //                        .license(new License()
                //                                .identifier("")
                //                                .name("")
                //                                .url("")
                //                        )
                //                ).externalDocs(new ExternalDocumentation()
                //                        .description("")
                //                        .url("")
                ).components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("api-auth")
                .pathsToMatch(
                        "/login",
                        "/login/google",
                        "/login/apple",
                        "/refresh",
                        "/confirm",
                        "/password-*")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("api-user")
                .pathsToMatch(
                        "/usuario/**",
                        "/pet/**",
                        "/coleira/**",
                        "/informacao/**",
                        "/assinatura/**")
                .build();
    }

    @Bean
    public GroupedOpenApi vetApi() {
        return GroupedOpenApi.builder()
                .group("api-vet")
                .pathsToMatch("/vet/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("api-admin")
                .pathsToMatch("/plano/**", "/firmware/**", "/log/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mqttApi() {
        return GroupedOpenApi.builder()
                .group("api-mqtt")
                .pathsToMatch("/mqtt/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api-public")
                .pathsToMatch("/public/**", "/enum/**", "/firmware/download/**")
                .build();
    }
}
