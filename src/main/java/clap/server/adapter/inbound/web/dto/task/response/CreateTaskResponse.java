package clap.server.adapter.inbound.web.dto.task.response;



public record CreateTaskResponse(
        Long taskId,
        Long categoryId,
        String title
) {
}
