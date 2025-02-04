package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendAgitService {

    private final SendAgitPort agitPort;

    public void sendAgit(PushNotificationTemplate request, Task task) {
        agitPort.sendAgit(request, task);
    }
}
