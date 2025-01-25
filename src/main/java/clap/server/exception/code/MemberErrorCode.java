package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "회원을 찾을 수 없습니다."),
    ACTIVE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_002", "활성화 회원을 찾을 수 없습니다."),
    INVALID_CSV_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER_003", "CSV 파일 형식이 잘못되었습니다."),
    CSV_PARSING_ERROR(HttpStatus.BAD_REQUEST, "MEMBER_004", "CSV 데이터 파싱 중 오류가 발생했습니다."),
    MEMBER_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER_005", "회원 등록 중 오류가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
