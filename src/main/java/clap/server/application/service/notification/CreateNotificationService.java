package clap.server.application.service.notification;

import clap.server.application.port.inbound.notification.CreateNotificationUsecase;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CreateNotificationService implements CreateNotificationUsecase {

    private final CommandNotificationPort commandNotificationPort;

    @Override
    public void createNotification(Notification request) {

        commandNotificationPort.save(request);
    }
}
