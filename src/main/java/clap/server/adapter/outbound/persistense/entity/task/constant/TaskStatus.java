package clap.server.adapter.outbound.persistense.entity.task.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    REQUESTED("요청"),
    IN_PROGRESS("진행 중"),
    PENDING_COMPLETED("검토 중"),
    COMPLETED("완료"),
    TERMINATED("종료");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }
    @JsonCreator
    public static TaskStatus fromDescription(String description) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + description);
    }
}
