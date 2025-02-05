package clap.server.adapter.outbound.persistense.entity.task.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    REQUESTED("요청"),
    IN_PROGRESS("진행 중"),
    IN_REVIEWING("완료 대기"),
    COMPLETED("완료"),
    TERMINATED("종료");

    private final String description;
}
