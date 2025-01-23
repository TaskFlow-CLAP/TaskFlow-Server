package clap.server.adapter.outbound.jwt.refresh;

import lombok.Getter;

@Getter
public enum RefreshTokenClaimKeys {
    USER_ID("id");

    private final String value;

    RefreshTokenClaimKeys(String value) {
        this.value = value;
    }
}
