package clap.server.application.service.notification;

import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class CreateNotificationService{

    private final CommandNotificationPort commandNotificationPort;

    @Transactional
    public void createNotification(Notification request) {

        commandNotificationPort.save(request);
    }
}
