package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatisticsErrorCode implements BaseErrorCode{
    STATISTICS_BAD_REQUEST(HttpStatus.BAD_REQUEST, "STATISTICS_001", "잘못된 통계 조회 파라미터 입력.");

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
