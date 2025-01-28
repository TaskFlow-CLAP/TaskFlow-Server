package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;

public interface UpdateTaskLabelUsecase {
    UpdateTaskResponse updateTaskLabel(Long taskId, Long userId, UpdateTaskLabelRequest request);
}
