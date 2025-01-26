package clap.server.common.utils;

import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;

public class InitialPasswordGenerator {

    @Value("${password.policy.characters}")
    private String characters;

    private static final int PASSWORD_LENGTH = 8;

    private InitialPasswordGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be greater than 0");
        }

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(PASSWORD_LENGTH);
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }
}
