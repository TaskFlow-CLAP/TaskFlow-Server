package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;

import java.util.List;

public interface FindTaskDetailsUsecase {
    FindTaskDetailsResponse findRequestedTaskDetails(Long memberId, Long taskId);
}
