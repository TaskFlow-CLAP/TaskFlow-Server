package clap.server.application.service.auth;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.adapter.outbound.jwt.access.AccessTokenClaim;
import clap.server.adapter.outbound.jwt.access.temporary.TemporaryTokenClaim;
import clap.server.adapter.outbound.jwt.refresh.RefreshTokenClaim;
import clap.server.adapter.outbound.jwt.refresh.RefreshTokenClaimKeys;
import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.auth.RefreshToken;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
@Slf4j
class ManageTokenService {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final JwtProvider temporaryTokenProvider;

    public CustomJwts issueTokens(Member member) {
        String accessToken = accessTokenProvider.createToken(AccessTokenClaim.of(member.getMemberId()));
        String refreshToken = refreshTokenProvider.createToken(RefreshTokenClaim.of(member.getMemberId()));
        return CustomJwts.of(accessToken, refreshToken);
    }

    public String issueAccessToken(Long memberId) {
        return accessTokenProvider.createToken(AccessTokenClaim.of(memberId));
    }

    public RefreshToken issueRefreshToken(Long memberId) {
        String refreshToken = refreshTokenProvider.createToken(RefreshTokenClaim.of(memberId));
        return RefreshToken.of(
                memberId, refreshToken,
                toSeconds(refreshTokenProvider.getExpiredDate(refreshToken))
        );
    }

    public String issueTemporaryToken(Long memberId) {
        return temporaryTokenProvider.createToken(TemporaryTokenClaim.of(memberId));
    }


    public Long resolveRefreshToken(String refreshToken) {
        JwtClaims claims = refreshTokenProvider.parseJwtClaimsFromToken(refreshToken);

        return getClaimValue(claims,
                RefreshTokenClaimKeys.USER_ID.getValue(),
                Long::parseLong);
    }

    public LocalDateTime getExpiredDate(String accessToken) {
        return accessTokenProvider.getExpiredDate(accessToken);
    }

    private long toSeconds(LocalDateTime expiredDate) {
        return Duration.between(LocalDateTime.now(), expiredDate).getSeconds();
    }

    private static <T> T getClaimValue(JwtClaims jwtClaims, String key, Function<String, T> converter) {
        Object value = jwtClaims.getClaims().get(key);
        if (value != null) {
            return converter.apply(value.toString());
        }
        return null;
    }

}
