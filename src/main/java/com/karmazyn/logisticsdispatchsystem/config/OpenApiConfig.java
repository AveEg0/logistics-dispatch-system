package com.karmazyn.logisticsdispatchsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Logistics Dispatch System API",
                version = "1.0.0",
                description = "Comprehensive API for managing users, drivers, and delivery orders. " +
                        "This system handles order creation, driver assignment, and real-time tracking.",
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Environment")
        }
)
@Configuration
public class OpenApiConfig {
}