package clap.server.common.constants;

import lombok.Getter;

@Getter
public enum AuthConstants {
    AUTHORIZATION("Authorization"), TOKEN_PREFIX("Bearer "), SESSION_ID("sessionId");

    private final String value;

    AuthConstants(String value) {
        this.value = value;
    }
}
