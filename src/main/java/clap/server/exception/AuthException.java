package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class AuthException extends BaseException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }

    public BaseErrorCode getErrorCode() {
        return (BaseErrorCode)super.getCode();
    }
}
