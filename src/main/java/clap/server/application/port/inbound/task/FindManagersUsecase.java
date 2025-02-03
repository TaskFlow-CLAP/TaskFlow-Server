package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.FindManagersResponse;
import java.util.List;

public interface FindManagersUsecase {
    List<FindManagersResponse> findManagers();
}
