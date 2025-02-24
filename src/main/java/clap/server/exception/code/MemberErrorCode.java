package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "회원을 찾을 수 없습니다."),
    ACTIVE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_002", "활성화 회원을 찾을 수 없습니다."),
    NOT_A_REVIEWER(HttpStatus.FORBIDDEN, "MEMBER_003", "승인 권한이 없습니다."),
    COMMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "MEMBER_005", "댓글 작성 권한이 없습니다."),
    PASSWORD_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "MEMBER_006", "비밀번호 검증에 실패하였습니다"),
    INVALID_CSV_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER_007", "CSV 파일 형식이 잘못되었습니다."),
    CSV_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER_008", "CSV 데이터 파싱 중 오류가 발생했습니다."),
    REVIEW_PERMISSION_DENIED(HttpStatus.BAD_REQUEST, "MEMBER_009", "담당자만 리뷰 권한이 있습니다."),
    NAME_CANNOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "MEMBER_010", "이름은 공백일 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"MEMBER_011", "중복된 닉네임입니다"),
    DUPLICATE_NICKNAME_OR_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_012", "중복된 닉네임이나 email이 존재합니다"),
    MANAGER_PERMISSION_DENIED(HttpStatus.BAD_REQUEST, "MEMBER_013", "담당자 권한이 없는 부서입니다."),
    INVALID_EMAIL_NICKNAME_MATCH(HttpStatus.BAD_REQUEST, "MEMBER_014", "닉네임과 이메일이 일치하지 않습니다"),
    MANAGER_MEMBER_UPDATE_NOT_ALLOWED_WITH_TASKS(HttpStatus.BAD_REQUEST, "MEMBER_015", "잔여 작업이 남아있어 수정이 불가합니다."),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER_016", "잘못된 닉네임 형식입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}