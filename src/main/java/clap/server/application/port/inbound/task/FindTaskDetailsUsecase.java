package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.FindTaskDetailsResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindTaskDetailsForManagerResponse;

public interface FindTaskDetailsUsecase {
    FindTaskDetailsResponse findRequestedTaskDetails(Long memberId, Long taskId);

    FindTaskDetailsForManagerResponse findTaskDetailsForManager(Long memberId, Long taskId);
}
