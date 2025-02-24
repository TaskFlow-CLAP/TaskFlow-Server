package clap.server.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_001", "알림을 찾을 수 없습니다"),
    SSE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_002", "SSE 초기 연결에 실패하였습니다"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_003", "이메일 알림 전송에 실패하였습니다"),
    KAKAO_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_004", "카카오워크 알림 전송에 실패하였습니다"),
    AGIT_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_005", "아지트 알림 전송에 실패하였습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;
}
