package clap.server.adapter.inbound.security.filter;
import clap.server.exception.JwtException;
import clap.server.exception.code.AuthErrorCode;
import clap.server.exception.code.BaseErrorCode;
import clap.server.exception.code.CommonErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SignatureException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtErrorCodeUtil {
    private static final Map<Class<? extends Exception>, BaseErrorCode> ERROR_CODE_MAP = Map.of(
            ExpiredJwtException.class, AuthErrorCode.EXPIRED_TOKEN,
            MalformedJwtException.class, AuthErrorCode.MALFORMED_TOKEN,
            SignatureException.class, AuthErrorCode.TAMPERED_TOKEN,
            UnsupportedJwtException.class, AuthErrorCode.UNSUPPORTED_JWT_TOKEN
    );

    public static BaseErrorCode determineErrorCode(Exception exception, BaseErrorCode defaultErrorCode) {
        if (exception instanceof JwtException jwtException)
            return jwtException.getErrorCode();

        Class<? extends Exception> exceptionClass = exception.getClass();
        return ERROR_CODE_MAP.getOrDefault(exceptionClass, defaultErrorCode);
    }


    public static JwtException determineAuthErrorException(Exception exception) {
        return findAuthErrorException(exception).orElseGet(
                () -> {
                    BaseErrorCode errorStatus = determineErrorCode(exception, CommonErrorCode.INTERNAL_SERVER_ERROR);
                    log.debug(exception.getMessage(), exception);
                    return new JwtException(errorStatus);
                }
        );
    }

    private static Optional<JwtException> findAuthErrorException(Exception exception) {
        if (exception instanceof JwtException) {
            return Optional.of((JwtException)exception);
        } else if (exception.getCause() instanceof JwtException) {
            return Optional.of((JwtException)exception.getCause());
        }
        return Optional.empty();
    }
}
