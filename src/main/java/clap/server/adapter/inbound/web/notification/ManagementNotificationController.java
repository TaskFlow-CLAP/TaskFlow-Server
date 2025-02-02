package clap.server.adapter.inbound.web.notification;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.notification.EnableAgitUsecase;
import clap.server.application.port.inbound.notification.EnableEmailUsecase;
import clap.server.application.port.inbound.notification.EnableKakaoUsecase;
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

@Tag(name = "04. Notification")
@WebAdapter
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class ManagementNotificationController {

    private final UpdateNotificationUsecase updateNotificationUsecase;
    private final EnableKakaoUsecase enableKakaoUsecase;
    private final EnableAgitUsecase enableAgitUsecase;
    private final EnableEmailUsecase enableEmailUsecase;

    @Operation(summary = "알림 목록에서 한 개 눌렀을 때 읽음 처리")
    @Parameter(name = "notificationId", description = "알림 고유 ID", required = true, in = ParameterIn.PATH)
    @PatchMapping("/{notificationId}")
    public void updateNotificationIsRead(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                         @PathVariable Long notificationId) {
        updateNotificationUsecase.updateNotification(userInfo.getUserId(), notificationId);
    }

    @Operation(summary = "알림 목록에서 전체 읽음 버튼을 눌렀을 때 전체 읽음 처리")
    @PatchMapping
    public void updateAllNotificationIsRead(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        updateNotificationUsecase.updateAllNotification(userInfo.getUserId());
    }

    @Operation(summary = "카카오 푸시 알림 활성화/비활성화 API", description = "알림 거부였을 시 -> 승인으로 변경, 알림 승인이였을 시 -> 거부로 변경")
    @PatchMapping("/kakao")
    public void enableKaKaoWork(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        enableKakaoUsecase.enableKakao(userInfo.getUserId());
    }

    @Operation(summary = "아지트 푸시 알림 활성화/비활성화 API", description = "알림 거부였을 시 -> 승인으로 변경, 알림 승인이였을 시 -> 거부로 변경")
    @PatchMapping("/agit")
    public void enableAgit(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        enableAgitUsecase.enableAgit(userInfo.getUserId());
    }

    @Operation(summary = "이메일 푸시 알림 활성화/비활성화 API", description = "알림 거부였을 시 -> 승인으로 변경, 알림 승인이였을 시 -> 거부로 변경")
    @PatchMapping("/email")
    public void enableEmail(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        enableEmailUsecase.enableEmail(userInfo.getUserId());
    }
}
