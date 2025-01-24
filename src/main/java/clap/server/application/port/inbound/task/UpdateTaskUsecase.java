package clap.server.application.port.inbound.task;


import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;

public interface UpdateTaskUsecase {
    UpdateTaskResponse updateTask(Long memberId, UpdateTaskRequest updateTaskRequest);
}
