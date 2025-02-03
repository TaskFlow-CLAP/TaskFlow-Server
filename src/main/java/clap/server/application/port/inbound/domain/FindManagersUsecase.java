package clap.server.application.port.inbound.domain;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import java.util.List;

public interface FindManagersUsecase {
    List<FindManagersResponse> execute();
}
