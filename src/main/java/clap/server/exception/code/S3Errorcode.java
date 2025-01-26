package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3Errorcode implements BaseErrorCode {
    FILE_UPLOAD_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TASK_004", "파일 업로드에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
