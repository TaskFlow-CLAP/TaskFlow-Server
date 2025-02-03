package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.UpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;

public interface UpdateTaskProcessorUsecase {
    UpdateTaskResponse updateTaskProcessor(Long taskId, Long userId, UpdateTaskProcessorRequest request);
}
