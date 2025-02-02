package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.response.UpdateTaskResponse;

public interface UpdateTaskLabelUsecase {
    UpdateTaskResponse updateTaskLabel(Long taskId, Long userId, UpdateTaskLabelRequest request);
}
