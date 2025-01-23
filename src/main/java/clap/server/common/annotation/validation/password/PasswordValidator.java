package clap.server.common.annotation.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}\\[\\]:;\"'<>,.?/\\\\|]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}
