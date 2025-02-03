package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

public interface UpdateTaskOrderAndStatusUsecase {
    void updateTaskOrderAndStatus(Long processorId, UpdateTaskOrderRequest request, TaskStatus status);
}
