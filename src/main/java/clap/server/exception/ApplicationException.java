package clap.server.exception;

import clap.server.exception.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final BaseErrorCode code;

    public ApplicationException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public static ApplicationException from(BaseErrorCode code) {
        return new ApplicationException(code);
    }
}
