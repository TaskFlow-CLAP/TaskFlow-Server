package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class JwtException extends BaseException {
    public JwtException(BaseErrorCode code) {
        super(code);
    }

    public BaseErrorCode getErrorCode() {
        return (BaseErrorCode)super.getCode();
    }
}