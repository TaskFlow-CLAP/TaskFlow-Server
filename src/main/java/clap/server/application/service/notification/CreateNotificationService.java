package clap.server.application.service.notification;

import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CreateNotificationService{

    private final CommandNotificationPort commandNotificationPort;

    public void createNotification(Notification request) {

        commandNotificationPort.save(request);
    }
}
