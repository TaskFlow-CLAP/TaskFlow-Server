package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class AdapterException extends BaseException {

    public AdapterException(BaseErrorCode code) {
        super(code);
    }

    public static AdapterException from(BaseErrorCode code) {
        return new AdapterException(code);
    }
}

