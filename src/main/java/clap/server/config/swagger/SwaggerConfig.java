package clap.server.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    @Profile("local")
    public OpenAPI localOpenAPI() {
        return createOpenAPI(getLocalServer());
    }

    @Bean
    @Profile("dev")
    public OpenAPI devOpenAPI() {
        return createOpenAPI(getDevServer());
    }

    private OpenAPI createOpenAPI(Server server) {
        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info().title("TaskFlow API").version("1.0"));
    }

    private Server getLocalServer() {
        return new Server()
                .url(serverUrl)
                .description("Local Server");
    }

    private Server getDevServer() {
        return new Server()
                .url(serverUrl)
                .description("Development Server");
    }
}