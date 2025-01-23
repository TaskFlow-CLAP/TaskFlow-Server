package clap.server.adapter.outbound.jwt.refresh;

import clap.server.adapter.outbound.jwt.JwtClaims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenClaim implements JwtClaims {
    private final Map<String, Object> claims;

    public static RefreshTokenClaim of(Long memberId) {
        Map<String, Object> claims = Map.of(
                RefreshTokenClaimKeys.USER_ID.getValue(), memberId.toString()
        );
        return new RefreshTokenClaim(claims);
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }
}
