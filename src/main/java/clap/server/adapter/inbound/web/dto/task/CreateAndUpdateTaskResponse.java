package clap.server.adapter.inbound.web.dto.task;



public record CreateAndUpdateTaskResponse(
        Long taskId,
        Long categoryId,
        String title
) {
}
