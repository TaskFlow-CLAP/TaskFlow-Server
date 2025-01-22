package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class DomainException extends BaseException {

    public DomainException(BaseErrorCode code) {
        super(code);
    }

    public static DomainException from(BaseErrorCode code) {
        return new DomainException(code);
    }
}
