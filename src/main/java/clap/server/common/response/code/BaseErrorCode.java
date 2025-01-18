package clap.server.common.response.code;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getCustomCode();
    String getMessage();
}
