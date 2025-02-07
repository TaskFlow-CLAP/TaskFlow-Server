package clap.server.common.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Component
public class InitialPasswordGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final int UPPER = 0, LOWER = 26, DIGIT = 52, SPECIAL = 62;
    private static final int PASSWORD_LENGTH = 8;

    private final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        char[] password = new char[PASSWORD_LENGTH];
        int[] cases = {UPPER, LOWER, DIGIT, SPECIAL};

        for (int i = 0; i < 4; i++) {
            int start = cases[i];
            int end = (i == 3) ? CHARS.length() : cases[i + 1];
            password[i] = CHARS.charAt(start + random.nextInt(end - start));
        }

        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password[i] = CHARS.charAt(random.nextInt(CHARS.length()));
        }

        for (int i = PASSWORD_LENGTH - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }
}


