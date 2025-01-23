package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;

public interface CreateTaskUsecase {
    CreateTaskResponse createTask(Long memberId, CreateTaskRequest createTaskRequest);
}
