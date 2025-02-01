package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorcode implements BaseErrorCode {
    FILE_UPLOAD_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_001", "파일 업로드에 실패하였습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "FILE_002", "이미지가 아닌 파일 유형입니다."),;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
