package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class ApplicationException extends BaseException {

    public ApplicationException(BaseErrorCode code) {
        super(code);
    }

    public static ApplicationException from(BaseErrorCode code) {
        return new ApplicationException(code);
    }
}
