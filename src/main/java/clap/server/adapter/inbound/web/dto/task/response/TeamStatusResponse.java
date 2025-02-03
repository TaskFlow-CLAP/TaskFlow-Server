package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamMemberTaskResponse> members
) {
    public TeamStatusResponse(List<TeamMemberTaskResponse> members) {
        this.members = (members == null) ? List.of() : members;
    }
}
