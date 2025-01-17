package clap.server.adapter.outbound.persistense.entity.task.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    REQUESTED("요청"),
    IN_PROGRESS("진행 중"),
    PENDING_COMPLETED("완료 대기"),
    COMPLETED("완료"),
    FAILED("실패");


    private final String description;
}
