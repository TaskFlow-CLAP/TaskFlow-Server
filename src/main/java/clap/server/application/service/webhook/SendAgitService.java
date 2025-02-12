package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendAgitService {

    private final SendAgitPort agitPort;
    private final TaskService taskService;

    @Transactional
    public void sendAgit(PushNotificationTemplate request, Task task, String taskDetailUrl) {
        Long agitPostId = agitPort.sendAgit(request, task, taskDetailUrl);

        if (request.notificationType().equals(NotificationType.TASK_REQUESTED)) {
            task.updateAgitPostId(agitPostId);
            taskService.upsert(task);
        }
    }
}
