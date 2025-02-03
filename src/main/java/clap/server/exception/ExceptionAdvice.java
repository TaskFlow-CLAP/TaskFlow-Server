package clap.server.exception;

import clap.server.exception.code.AuthErrorCode;
import clap.server.exception.code.BaseErrorCode;
import clap.server.exception.code.GlobalErrorCode;
import clap.server.exception.code.MemberErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(basePackages = {"clap.server.adapter.inbound.web"})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.error("Validation error occurred: {}", e.getMessage(), e); // 로그 추가

        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(
                e,
                HttpHeaders.EMPTY,
                GlobalErrorCode.INTERNAL_SERVER_ERROR,
                request,
                errors
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        log.error("ConstraintViolationException occurred: {}", e.getMessage(), e); // 로그 추가

        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException Error"));

        return handleExceptionInternalConstraint(
                e,
                GlobalErrorCode.valueOf(errorMessage),
                HttpHeaders.EMPTY,
                request
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e); // 로그 추가

        return handleExceptionInternalFalse(
                e,
                GlobalErrorCode.INTERNAL_SERVER_ERROR,
                HttpHeaders.EMPTY,
                GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
                request,
                e.getMessage()
        );
    }

    @ExceptionHandler(value = { BaseException.class })
    public ResponseEntity<Object> onThrowException(BaseException exception, HttpServletRequest request) {
        BaseErrorCode baseErrorCode = exception.getCode();
        log.error("BaseException occurred: {}", baseErrorCode.getMessage());
        return handleExceptionInternal(exception, baseErrorCode, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            BaseException e,
            BaseErrorCode baseErrorCode,
            HttpHeaders headers,
            HttpServletRequest request
    ) {
        String code = baseErrorCode.getCustomCode();
        WebRequest webRequest = new ServletWebRequest(request);

        return super.handleExceptionInternal(
                e,
                code,
                headers,
                baseErrorCode.getHttpStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
            Exception e,
            BaseErrorCode baseErrorCode,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request,
            String errorPoint
    ) {
        String code = baseErrorCode.getCustomCode();

        return super.handleExceptionInternal(
                e,
                code,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            Exception e,
            HttpHeaders headers,
            BaseErrorCode baseErrorCode,
            WebRequest request,
            Map<String, String> errorArgs
    ) {
        String code = baseErrorCode.getCustomCode();

        return super.handleExceptionInternal(
                e,
                code,
                headers,
                baseErrorCode.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
            Exception e,
            BaseErrorCode baseErrorCode,
            HttpHeaders headers,
            WebRequest request
    ) {
        String code = baseErrorCode.getCustomCode();

        return super.handleExceptionInternal(
                e,
                code,
                headers,
                baseErrorCode.getHttpStatus(),
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        return handleExceptionInternalFalse(
                e,
                AuthErrorCode.FORBIDDEN,
                HttpHeaders.EMPTY,
                HttpStatus.FORBIDDEN,
                request,
                AuthErrorCode.FORBIDDEN.getMessage()
        );
    }
}
