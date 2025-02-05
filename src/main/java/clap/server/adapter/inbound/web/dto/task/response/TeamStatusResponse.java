package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamTaskResponse> members,
        int totalInProgressTaskCount,
        int totalPendingTaskCount,
        int totalTaskCount
) {

    public TeamStatusResponse(List<TeamTaskResponse> members, int totalInProgressTaskCount, int totalPendingTaskCount) {
        this(
                (members == null) ? List.of() : members,
                totalInProgressTaskCount,
                totalPendingTaskCount,
                totalInProgressTaskCount + totalPendingTaskCount
        );
    }

    public TeamStatusResponse(List<TeamTaskResponse> members) {
        this(members != null ? members : List.of(), 0, 0); // 기본값을 사용하여 생성
    }
}
