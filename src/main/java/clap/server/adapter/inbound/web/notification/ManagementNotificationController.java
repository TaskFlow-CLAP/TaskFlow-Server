package clap.server.adapter.inbound.web.notification;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.notification.UpdateNotificationUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04. Notification", description = "알림 읽음 처리 API")
@WebAdapter
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class ManagementNotificationController {

    private final UpdateNotificationUsecase updateNotificationUsecase;

    @Operation(summary = "알림 목록에서 한 개 눌렀을 때 읽음 처리")
    @Parameter(name = "notificationId", description = "알림 고유 ID", required = true, in = ParameterIn.PATH)
    @PatchMapping("/{notificationId}")
    public void updateNotificationIsRead(@PathVariable Long notificationId) {
        updateNotificationUsecase.updateNotification(notificationId);
    }

    @Operation(summary = "알림 목록에서 전체 읽음 버튼을 눌렀을 때 전체 읽음 처리")
    @PatchMapping
    public void updateAllNotificationIsRead(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        updateNotificationUsecase.updateAllNotification(userInfo.getUserId());
    }
}
