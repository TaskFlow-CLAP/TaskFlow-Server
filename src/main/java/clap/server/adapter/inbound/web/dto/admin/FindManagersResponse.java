package clap.server.adapter.inbound.web.dto.admin;

import java.util.List;

public record FindManagersResponse(
        Long memberId,
        String nickname,
        String imageUrl,
        int remainingTasks
) {

    public static List<FindManagersResponse> emptyListResponse() {
        return List.of();
    }
}
