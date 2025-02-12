package clap.server.application.service.task;

import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.CancelTaskUsecase;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@ApplicationService
@RequiredArgsConstructor
@Transactional
public class CancelTaskService implements CancelTaskUsecase {
    private final TaskService taskService;
    private final LoadNotificationPort loadNotificationPort;
    private final CommandNotificationPort commandNotificationPort;

    @Override
    public void cancleTask(Long taskId, Long memberId) {
        Task task = taskService.findById(taskId);
        deleteNotification(task.getTaskId());
        task.cancelTask(memberId);
        taskService.upsert(task);
    }

    private void deleteNotification(Long taskId) {
        List<Notification> notificationList =  loadNotificationPort.findNotificationsByTaskId(taskId);

        notificationList.forEach(commandNotificationPort::delete);
    }

}
