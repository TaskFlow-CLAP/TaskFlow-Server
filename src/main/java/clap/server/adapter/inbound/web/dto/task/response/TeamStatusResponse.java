package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamTaskResponse> members
) {
    public TeamStatusResponse(List<TeamTaskResponse> members) {
        this.members = (members == null) ? List.of() : members;
    }
}
