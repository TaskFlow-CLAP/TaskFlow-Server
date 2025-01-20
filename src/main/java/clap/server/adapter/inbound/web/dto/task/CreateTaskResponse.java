package clap.server.adapter.inbound.web.dto.task;



public record CreateTaskResponse(
        Long taskId,
        Long categoryId,
        String title
) {
}
