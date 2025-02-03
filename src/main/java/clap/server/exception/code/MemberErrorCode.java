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
    NOT_A_COMMENTER(HttpStatus.FORBIDDEN, "MEMBER_005", "댓글 권한이 없습니다."),
    PASSWORD_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "MEMBER_006", "비밀번호 검증에 실패하였습니다"),
    INVALID_CSV_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER_007", "CSV 파일 형식이 잘못되었습니다."),
    CSV_PARSING_ERROR(HttpStatus.BAD_REQUEST, "MEMBER_008", "CSV 데이터 파싱 중 오류가 발생했습니다."),
    MEMBER_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER_009", "회원 등록 중 오류가 발생했습니다."),
    NAME_CANNOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "MEMBER_010", "이름은 공백일 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
