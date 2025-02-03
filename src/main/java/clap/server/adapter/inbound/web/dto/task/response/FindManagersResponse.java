package clap.server.adapter.inbound.web.dto.task.response;

public record FindManagersResponse(
        Long memberId,
        String nickname,
        String imageUrl,
        int remainingTasks
) {
}
