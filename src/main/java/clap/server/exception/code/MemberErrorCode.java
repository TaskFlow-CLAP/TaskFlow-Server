package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "회원을 찾을 수 없습니다."),
    ACTIVE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_002", "활성화 회원을 찾을 수 없습니다."),
    NOT_A_REVIEWER(HttpStatus.FORBIDDEN, "MEMBER_003", "리뷰어 권한이 없습니다."),
    MEMBER_REGISTER_FAILED(HttpStatus.BAD_REQUEST, "MEMBER_004", "회원 등록에 실패하였습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
