package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.notification.response.CountNotificationResponse;
import clap.server.application.mapper.response.NotificationResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.notification.CountNotificationUseCase;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CountNotificationService implements CountNotificationUseCase {

    private final MemberService memberService;
    private final LoadNotificationPort loadNotificationPort;

    @Transactional
    @Override
    public CountNotificationResponse countNotification(Long memberId) {

        memberService.findActiveMember(memberId);
        Integer count = loadNotificationPort.countNotification(memberId);

        return NotificationResponseMapper.toCountNotificationResponse(memberId, count);
    }
}
