package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.notification.response.CountNotificationResponse;
import clap.server.application.mapper.NotificationMapper;
import clap.server.application.port.inbound.notification.CountNotificationUseCase;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CountNotificationService implements CountNotificationUseCase {

    private final LoadNotificationPort loadNotificationPort;

    @Transactional
    @Override
    public CountNotificationResponse countNotification(Long userId) {
        Integer count = loadNotificationPort.countNotification(userId);

        return NotificationMapper.toCountNotificationResponse(userId, count);
    }
}
