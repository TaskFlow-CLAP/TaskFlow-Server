package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateAndUpdateTaskResponse;

public interface CreateTaskUsecase {
    CreateAndUpdateTaskResponse createTask(Long memberId, CreateTaskRequest createTaskRequest);
}
