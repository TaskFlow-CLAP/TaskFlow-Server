package clap.server.adapter.inbound.web.dto.task;

public record UpdateTaskResponse(
    Long taskId,
    Long categoryId,
    String title
) {
}
