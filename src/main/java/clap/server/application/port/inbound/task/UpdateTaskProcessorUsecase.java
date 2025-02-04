package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;

public interface UpdateTaskProcessorUsecase {
    void updateTaskProcessor(Long taskId, Long userId, UpdateTaskProcessorRequest request);
}
