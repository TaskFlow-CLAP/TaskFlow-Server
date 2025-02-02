package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;

public interface UpdateTaskBoardUsecase {
    void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request);
}
