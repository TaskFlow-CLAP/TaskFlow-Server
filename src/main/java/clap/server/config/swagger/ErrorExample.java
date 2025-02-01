package clap.server.config.swagger;

public record ErrorExample(
        int code,
        String customCode,
        String message
) {
}
