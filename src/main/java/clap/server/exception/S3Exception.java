package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;

public class S3Exception extends BaseException {
    public S3Exception(BaseErrorCode code) {
        super(code);
    }

    public BaseErrorCode getErrorCode() {
        return (BaseErrorCode)super.getCode();
    }
}