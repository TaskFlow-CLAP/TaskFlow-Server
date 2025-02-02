package clap.server.adapter.inbound.web.dto.task.response;

public record UpdateTaskResponse(
    Long taskId,
    Long categoryId,
    String title
) {
}
