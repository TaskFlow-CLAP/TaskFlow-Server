package clap.server.common.utils;

import clap.server.common.properties.PasswordPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@RequiredArgsConstructor
public class InitialPasswordGenerator {

    private final PasswordPolicyProperties properties;

    private InitialPasswordGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be greater than 0");
        }

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        String characters = properties.getCharacters();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(properties.getLength());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }
}
