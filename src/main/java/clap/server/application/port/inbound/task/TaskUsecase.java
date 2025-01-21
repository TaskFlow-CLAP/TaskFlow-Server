package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskUsecase {
    CreateTaskResponse createTask(Long memberId, CreateTaskRequest createTaskRequest);

    Page<FindTaskListResponse> findRequestedTaskList(Long memberId, Pageable pageable, FindTaskListRequest findTaskListRequest);
}
