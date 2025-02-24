package clap.server.adapter.outbound.persistense.entity.task.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskHistoryType {
    COMMENT("댓글"),
    COMMENT_FILE("댓글 첨부파일"),
    STATUS_SWITCHED("상태 전환"),
    PROCESSOR_ASSIGNED("담당자 할당"),
    PROCESSOR_CHANGED("담당자 변경"),
    TASK_TERMINATED("작업 종료됨");

    private final String description;
}
