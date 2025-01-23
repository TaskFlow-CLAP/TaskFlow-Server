package clap.server.application.port.inbound.task;


import clap.server.adapter.inbound.web.dto.task.CreateAndUpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;

public interface UpdateTaskUsecase {
    CreateAndUpdateTaskResponse updateTask(Long memberId, UpdateTaskRequest updateTaskRequest);
}
