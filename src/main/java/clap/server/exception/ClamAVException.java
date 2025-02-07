package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class ClamAVException extends BaseException {
    public ClamAVException(BaseErrorCode code) {
        super(code);
    }

    public BaseErrorCode getErrorCode() {
        return (BaseErrorCode)super.getCode();
    }
}