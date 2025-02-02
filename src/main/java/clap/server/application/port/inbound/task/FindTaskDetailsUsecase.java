package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsForManagerResponse;

public interface FindTaskDetailsUsecase {
    FindTaskDetailsResponse findRequestedTaskDetails(Long memberId, Long taskId);

    FindTaskDetailsForManagerResponse findTaskDetailsForManager(Long memberId, Long taskId);
}
