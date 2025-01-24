package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class StatisticsException extends BaseException {

    public StatisticsException(BaseErrorCode code) {
        super(code);
    }
}
