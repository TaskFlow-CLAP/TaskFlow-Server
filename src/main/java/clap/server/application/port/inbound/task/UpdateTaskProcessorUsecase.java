package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskProcessorRequest;

public interface UpdateTaskProcessorUsecase {
    UpdateTaskResponse updateTaskProcessor(Long taskId, Long userId, UpdateTaskProcessorRequest request);
}
