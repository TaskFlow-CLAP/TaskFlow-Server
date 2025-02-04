package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.UpdateTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

public interface UpdateTaskStatusUsecase {
    void updateTaskStatus(Long memberId, Long taskId, TaskStatus taskStatus);
}
