package clap.server.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_001", "댓글을 찾을 수 없습니다."),
    COMMENT_ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_002", "댓글 첨부파일을 찾을 수 없습니다."),
    NOT_A_COMMENT_WRITER(HttpStatus.FORBIDDEN, "COMMENT_003", "댓글 작성자가 아닙니다.")
    ;


    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
