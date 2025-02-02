package clap.server.application.service.notification;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.notification.UpdateNotificationUsecase;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class ReadNotificationService implements UpdateNotificationUsecase {

    private final MemberService memberService;
    private final LoadNotificationPort loadNotificationPort;
    private final CommandNotificationPort commandNotificationPort;


    @Transactional
    @Override
    public void updateNotification(Long userId, Long notificationId) {

        memberService.findActiveMember(userId);
        Notification notification = loadNotificationPort.findById(notificationId)
                .orElseThrow(() -> new ApplicationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
        notification.updateNotificationIsRead();
        commandNotificationPort.save(notification);
    }

    @Transactional
    @Override
    public void updateAllNotification(Long memberId) {
        memberService.findActiveMember(memberId);
        List<Notification> notificationList = loadNotificationPort.findNotificationsByMemberId(memberId);
        for (Notification notification : notificationList) {
            notification.updateNotificationIsRead();
            commandNotificationPort.save(notification);
        }
    }
}
