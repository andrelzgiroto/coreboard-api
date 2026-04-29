package com.coreboard.api.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "CoreBoard API",
                version = "v1.0.0",
                contact = @Contact(
                        name = "André Giroto",
                        email = "andrelzgiroto@gmail.com")
        ), security = {
        @SecurityRequirement(name = "bearerAuth")
}
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfiguration {

    @Bean
    public OpenApiCustomizer cleanTrashSpringDoc() {
        return openApi -> {
            var schemas = openApi.getComponents().getSchemas();

            if (schemas != null) {

                schemas.values().forEach(schema -> {
                    if (schema.getProperties() != null) {
                        schema.getProperties().remove("pageable");
                        schema.getProperties().remove("sort");
                    }
                });

                schemas.remove("Pageablenull");
                schemas.remove("Sortnull");
            }
        };
    }
}
