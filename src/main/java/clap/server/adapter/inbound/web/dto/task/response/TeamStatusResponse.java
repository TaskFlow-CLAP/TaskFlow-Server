package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamMemberTaskResponse> members,
        boolean hasNext,
        boolean isFirst,
        boolean isLast
) {
    public TeamStatusResponse(List<TeamMemberTaskResponse> members, int pageNumber, int pageSize) {
        this(
                (members == null) ? List.of() : members,
                (members != null && members.size() > pageSize),
                pageNumber == 0,
                (members == null || members.size() <= pageSize)
        );
    }
}

