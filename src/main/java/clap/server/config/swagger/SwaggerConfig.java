package clap.server.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import static clap.server.common.AuthConstants.AUTHORIZATION;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "TaskFlow API";
    private static final String API_VERSION = "1.0.0";

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                .components(getComponents())
                .servers(List.of(getServer()))
                .security(getSecurity())
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title(API_NAME)
                .version(API_VERSION);
    }

    private static List<SecurityRequirement> getSecurity() {
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(AUTHORIZATION.getValue());

        return Collections.singletonList(securityRequirement);
    }

    private Server getServer() {
        return new Server()
                .url(serverUrl);
    }

    private static Components getComponents() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").name(AUTHORIZATION.getValue())
                .in(SecurityScheme.In.HEADER).name(AUTHORIZATION.getValue());

        return new Components()
                .addSecuritySchemes(AUTHORIZATION.getValue(), securityScheme);
    }
}