package clap.server.domain.policy.member;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PasswordPolicyTest {

    private PasswordPolicy passwordPolicy;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordPolicy = new PasswordPolicy();
        context = mock(ConstraintValidatorContext.class); // Mock ConstraintValidatorContext
    }

    @Test
    @DisplayName("유효한 비밀번호 - 대문자, 소문자, 숫자, 특수문자 포함")
    void validPassword() {
        String validPassword = "Abcdef1!";
        boolean result = passwordPolicy.isValid(validPassword, context);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 대문자 없음")
    void invalidPassword_noUpperCase() {
        String invalidPassword = "abcdef1!";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 소문자 없음")
    void invalidPassword_noLowerCase() {
        String invalidPassword = "ABCDEF1!";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 숫자 없음")
    void invalidPassword_noDigit() {
        String invalidPassword = "Abcdefgh!";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 특수문자 없음")
    void invalidPassword_noSpecialCharacter() {
        String invalidPassword = "Abcdefg1";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 길이가 8자 미만")
    void invalidPassword_tooShort() {
        String invalidPassword = "Ab1!";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - 길이가 20자 초과")
    void invalidPassword_tooLong() {
        String invalidPassword = "Abcdefg1!Abcdefg1!Abcdefg1!";
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 - null 값")
    void invalidPassword_null() {
        String invalidPassword = null;
        boolean result = passwordPolicy.isValid(invalidPassword, context);
        assertThat(result).isFalse();
    }
}
