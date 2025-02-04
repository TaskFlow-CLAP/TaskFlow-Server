package clap.server.adapter.inbound.web.dto.notification.response;


import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FindNotificationListResponse(
        @Schema(description = "알림 고유 ID", example = "1")
        Long notificationId,
        @Schema(description = "알림에 해당하는 작업 고유 ID", example = "1")
        Long taskId,
        @Schema(description = "알림 유형", example = "COMMENT or TASK_REQUESTED or STATUS_SWITCHED or " +
                                                    "PROCESSOR_ASSIGNED or PROCESSOR_CHANGED")
        NotificationType notificationType,
        @Schema(description = "알림 받는 회원 고유 ID", example = "1")
        Long receiverId,
        @Schema(description = "알림 제목", example = "VM 생성해주세요")
        String taskTitle,
        @Schema(description = "알림 내용", example = "진행 중 or 담당자 이름 등등")
        String message,
        @Schema(description = "읽음 여부", example = "false")
        Boolean isRead,
        @Schema(description = "알림 생성 시간", example = "2025-01-24 14:58")
        LocalDateTime createdAt
) {
}
