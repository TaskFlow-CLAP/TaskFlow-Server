package clap.server.application.service.task;

import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.CancelTaskUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;


@ApplicationService
@RequiredArgsConstructor
public class CancelTaskService implements CancelTaskUsecase {
    private final TaskService taskService;

    @Override
    public void cancleTask(Long taskId) {
        Task task = taskService.findById(taskId);
        task.cancelTask();
        taskService.upsert(task);
    }
}
