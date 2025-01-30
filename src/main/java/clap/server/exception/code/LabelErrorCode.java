package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LabelErrorCode implements BaseErrorCode{
    LABEL_NOT_FOUND(HttpStatus.NOT_FOUND, "LABEL_001", "작업 구분을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
