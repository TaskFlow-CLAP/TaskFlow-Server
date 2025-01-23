package clap.server.adapter.outbound.jwt.access.temporary;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.adapter.outbound.jwt.access.AccessTokenClaimKeys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporaryTokenClaim implements JwtClaims {
    private final Map<String, Object> claims;

    public static TemporaryTokenClaim of(Long memberId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AccessTokenClaimKeys.USER_ID.getValue(), memberId.toString());
        claims.put(TemporaryTokenClaimKeys.IS_TEMPORARY.getValue(), true);
        return new TemporaryTokenClaim(claims);
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    public Long getMemberId() {
        return Long.parseLong((String) claims.get(AccessTokenClaimKeys.USER_ID.getValue()));
    }

    public boolean isTemporary() {
        return (boolean) claims.get(TemporaryTokenClaimKeys.IS_TEMPORARY.getValue());
    }
}
