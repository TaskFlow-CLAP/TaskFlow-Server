package clap.server.adapter.inbound.web.notification;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.response.CountNotificationResponse;
import clap.server.adapter.inbound.web.dto.notification.response.FindNotificationListResponse;
import clap.server.application.port.inbound.notification.CountNotificationUseCase;
import clap.server.application.port.inbound.notification.FindNotificationListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04. Notification")
@WebAdapter
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class FindNotificationController {

    private final FindNotificationListUsecase findNotificationListUsecase;
    private final CountNotificationUseCase countNotificationUseCase;

    @Operation(summary = "알림 목록 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "조회할 목록 페이지 번호(0부터 시작)", example = "0", required = false),
            @Parameter(name = "size", description = "조회할 목록 페이지 당 개수", example = "5", required = false)
    })
    @GetMapping
    public ResponseEntity<SliceResponse<FindNotificationListResponse>> findNotificationList(
            @AuthenticationPrincipal SecurityUserDetails securityUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(findNotificationListUsecase.findNotificationList(securityUserDetails.getUserId(), pageable));
    }

    @Operation(summary = "미확인 알림 개수 조회 API")
    @GetMapping("/count")
    public ResponseEntity<CountNotificationResponse> countNotification(
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(countNotificationUseCase.countNotification(userInfo.getUserId()));
    }
}
