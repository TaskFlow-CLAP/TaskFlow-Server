package clap.server.common.utils;

import java.util.Random;

public class VerificationCodeGenerator {

    public static String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
