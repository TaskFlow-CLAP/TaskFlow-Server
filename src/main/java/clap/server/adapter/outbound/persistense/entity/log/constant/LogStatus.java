package clap.server.adapter.outbound.persistense.entity.log.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogStatus {
    LOGIN("로그인"),
    REQUEST_CREATED("요청 생성"),
    REQUEST_UPDATED("요청 수정"),
    REQUEST_CANCELLED("요청 취소"),
    REQUEST_APPROVED("요청 승인"),
    ASSIGNER_CHANGED("담당자 변경"),
    COMMENT_ADDED("댓글 추가"),
    COMMENT_UPDATED("댓글 수정"),
    STATUS_CHANGED("작업 상태 변경"),
    TASK_VIEWED("작업 조회");

    private final String description;
}
