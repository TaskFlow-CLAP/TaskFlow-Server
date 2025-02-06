package clap.server.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증 과정에서 오류가 발생하였습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_002", "접근이 거부되었습니다"),
    EMPTY_ACCESS_KEY(HttpStatus.FORBIDDEN, "AUTH_003", "AccessToken 이 비어있습니다."),
//    LOGOUT_ERROR(HttpStatus.FORBIDDEN, "AUTH_004", "로그 아웃된 사용자입니다."),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "AUTH_005", "사용기간이 만료된 토큰입니다."),
    TAKEN_AWAY_TOKEN(HttpStatus.FORBIDDEN, "AUTH_006", "탈취당한 토큰입니다. 다시 로그인 해주세요."),
    WITHOUT_OWNER_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "AUTH_007", "소유자가 아닌 RefreshToken 입니다."),
    EXPIRATION_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "AUTH_008", "RefreshToken 이 만료되었습니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_009", "비정상적인 토큰입니다."),
    TAMPERED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_010", "서명이 조작된 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_011", "지원하지 않는 토큰입니다."),
    FORBIDDEN_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "AUTH_012","해당 토큰에는 엑세스 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"AUTH_013", "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_014", "리프레시 토큰을 찾을 수 없습니다."),
    ACCOUNT_IS_LOCKED(HttpStatus.UNAUTHORIZED, "AUTH_015", "접근할 수 없는 계정입니다."),
    LOGIN_REQUEST_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_016", "로그인에 실패하였습니다."),
    REFRESH_TOKEN_MISMATCHED(HttpStatus.UNAUTHORIZED, "AUTH_017", "리프레시 토큰이 일치하지 않습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}


