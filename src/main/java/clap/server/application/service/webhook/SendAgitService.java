package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendAgitService {

    private final SendAgitPort agitPort;
    private final TaskService taskService;

    public void sendAgit(PushNotificationTemplate request, Task task) {
        Long agitPostId = agitPort.sendAgit(request, task);

        if (request.notificationType().equals(NotificationType.TASK_REQUESTED)) {
            task.updateAgitPostId(agitPostId);
            taskService.upsert(task);
        }
    }
}
