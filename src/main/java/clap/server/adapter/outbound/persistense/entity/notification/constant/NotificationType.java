package clap.server.adapter.outbound.persistense.entity.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    COMMENT("댓글"),
    TASK_REQUESTED("작업 요청"),
    STATUS_SWITCHED("상태 전환"),
    PROCESSOR_ASSIGNED("담당자 할당"),
    PROCESSOR_CHANGED("담당자 변경"),
    INVITATION("회원가입 초대");

    private final String description;
}
