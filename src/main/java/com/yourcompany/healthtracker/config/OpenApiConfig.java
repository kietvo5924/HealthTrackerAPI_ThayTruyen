package com.yourcompany.healthtracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API theo dõi sức khỏe và thể chất",
                version = "1.0.0",
                description = "Tài liệu API cho hệ thống theo dõi sức khỏe và thể chất."
        ),
        servers = {
                @Server(url = "https://healthtrackerapi-thaytruyen.onrender.com", description = "Local Development Server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
