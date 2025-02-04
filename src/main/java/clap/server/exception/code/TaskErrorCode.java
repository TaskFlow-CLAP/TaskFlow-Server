package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements BaseErrorCode {
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_001", "작업을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_002", "카테고리를 찾을 수 없습니다."),
    TASK_STATUS_MISMATCH(HttpStatus.BAD_REQUEST, "TASK_003", "작업 상태가 일치하지 않습니다."),
    TASK_ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_004", "첨부파일을 찾을 수 없습니다."),
    LABEL_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_005", "작업구분을 찾을 수 없습니다."),
    NOT_A_PROCESSOR(HttpStatus.FORBIDDEN, "TASK_006", "작업 처리 및 수정 권한이 없습니다."),
    INVALID_TASK_ORDER(HttpStatus.INTERNAL_SERVER_ERROR, "TASK_007", "유효하지 않은 task order입니다."),
    INVALID_TASK_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "TASK_008", "유효하지 않은 작업 상태 전환입니다."),
    NOT_A_REVIEWER(HttpStatus.FORBIDDEN, "TASK_009", "작업 승인 및 수정 권한이 없습니다."),
    NOT_A_REQUESTER(HttpStatus.FORBIDDEN, "TASK_009", "작업 수정 권한이 없습니다."),
    TASK_STATUS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "TASK_010", "변경할 수 없는 작업 상태입니다. 다른 API를 사용해주세요")
    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
