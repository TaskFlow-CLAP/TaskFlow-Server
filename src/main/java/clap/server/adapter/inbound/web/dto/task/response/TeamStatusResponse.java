package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamMemberTaskResponse> members,
        int totalInProgressTaskCount,
        int totalPendingTaskCount,
        int totalTaskCount
) {
    // 기존 생성자 (3개 파라미터)
    public TeamStatusResponse(List<TeamMemberTaskResponse> members, int totalInProgressTaskCount, int totalPendingTaskCount) {
        this(
                (members == null) ? List.of() : members,
                totalInProgressTaskCount,
                totalPendingTaskCount,
                totalInProgressTaskCount + totalPendingTaskCount
        );
    }

    // 추가된 생성자 (List만 받음)
    public TeamStatusResponse(List<TeamMemberTaskResponse> members) {
        this(members, 0, 0); // 기본값을 사용하여 생성
    }
}
