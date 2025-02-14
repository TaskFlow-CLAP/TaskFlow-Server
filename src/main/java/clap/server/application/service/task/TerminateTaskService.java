package clap.server.application.service.task;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.TerminateTaskUsecase;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional
public class TerminateTaskService implements TerminateTaskUsecase {
    private final TaskService taskService;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendNotificationService sendNotificationService;
    private final UpdateProcessorTaskCountService updateProcessorTaskCountService;

    @Override
    public void terminateTask(Long memberId, Long taskId, String reason) {
        Task task = taskService.findById(taskId);

        updateProcessorTaskCountService.handleTaskStatusChange(task.getProcessor(), task.getTaskStatus(), TaskStatus.TERMINATED);
        task.terminateTask();
        taskService.upsert(task);

        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.TASK_TERMINATED, task, reason, null, null);
        commandTaskHistoryPort.save(taskHistory);

        publishNotification(task.getRequester(), task, task.getTaskStatus().getDescription(), reason);

    }

    private void publishNotification(Member receiver, Task task, String message, String reason) {
        sendNotificationService.sendPushNotification(receiver, NotificationType.STATUS_SWITCHED, task, message, reason, null, false);
        sendNotificationService.sendAgitNotification(NotificationType.STATUS_SWITCHED, task, message, null);
    }
}
