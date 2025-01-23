package clap.server.adapter.outbound.jwt.access;

import clap.server.adapter.outbound.jwt.JwtClaims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessTokenClaim implements JwtClaims {
    private final Map<String, Object> claims;

    public static AccessTokenClaim of(Long memberId) {
        Map<String, Object> claims = Map.of(
                AccessTokenClaimKeys.USER_ID.getValue(), memberId.toString()
        );
        return new AccessTokenClaim(claims);
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }
}
