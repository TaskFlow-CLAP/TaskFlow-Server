package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.domain.model.task.Task;

public interface SendAgitPort {
    void sendAgit(PushNotificationTemplate request, Task task);
}
