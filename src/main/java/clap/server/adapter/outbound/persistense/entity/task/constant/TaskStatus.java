package clap.server.adapter.outbound.persistense.entity.task.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    REQUESTED("요청"),
    IN_PROGRESS("진행 중"),
    PENDING_COMPLETED("완료 대기"),
    COMPLETED("완료"),
    TERMINATED("종료");

    private final String description;
}
