package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskStateRequest;

public interface UpdateTaskStatusUsecase {
    UpdateTaskResponse updateTaskState(Long memberId, Long taskId, UpdateTaskStateRequest updateTaskStateRequest);
}
