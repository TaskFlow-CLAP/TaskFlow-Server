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
    TASK_ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_004", "첨부파일을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
