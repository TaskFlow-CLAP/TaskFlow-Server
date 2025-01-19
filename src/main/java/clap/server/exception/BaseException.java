package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;
import lombok.Getter;


@Getter
public abstract class BaseException extends RuntimeException {

    private final BaseErrorCode code;

    protected BaseException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public static <T extends BaseException> T from(BaseErrorCode code, Class<T> exceptionClass) {
        try {
            return exceptionClass.getConstructor(BaseErrorCode.class).newInstance(code);
        } catch (Exception e) {
            throw new RuntimeException("Could not create exception instance", e);
        }
    }
}
