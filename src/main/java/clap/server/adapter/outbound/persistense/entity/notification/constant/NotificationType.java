package clap.server.adapter.outbound.persistense.entity.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    COMMENT("댓글"),
    TASK_REQUESTED("작업 요청"),
    STATUS_SWITCHED("상태 전환"),
    PROCESSOR_ASSIGNED("처리자 할당"),
    PROCESSOR_CHANGED("처리자 변경");

    private final String description;
}
