package clap.server.adapter.outbound.jwt.access.temporary;

import lombok.Getter;

@Getter
public enum TemporaryTokenClaimKeys {
    IS_TEMPORARY("isTemporary");

    private final String value;

    TemporaryTokenClaimKeys(String value) {
        this.value = value;
    }
}