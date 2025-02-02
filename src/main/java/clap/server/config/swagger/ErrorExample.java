package clap.server.config.swagger;

public record ErrorExample(
        int code,
        String name,
        String message
) {
}
