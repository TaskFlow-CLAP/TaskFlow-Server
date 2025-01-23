package clap.server.application.service.auth;

import clap.server.adapter.outbound.jwt.access.AccessTokenClaim;
import clap.server.adapter.outbound.jwt.access.temporary.TemporaryTokenClaim;
import clap.server.adapter.outbound.jwt.refresh.RefreshTokenClaim;
import clap.server.application.port.outbound.auth.CommandRefreshTokenPort;
import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.application.port.outbound.auth.LoadRefreshTokenPort;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.auth.RefreshToken;
import clap.server.domain.model.member.Member;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
class IssueTokenService {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final JwtProvider temporaryTokenProvider;
    private final LoadRefreshTokenPort loadRefreshTokenPort;
    private final CommandRefreshTokenPort commandRefreshTokenPort;

    public CustomJwts createToken(Member member) {
        String accessToken = accessTokenProvider.createToken(AccessTokenClaim.of(member.getMemberId()));
        String refreshToken = refreshTokenProvider.createToken(RefreshTokenClaim.of(member.getMemberId()));

        commandRefreshTokenPort.save(
                RefreshToken.of(
                        member.getMemberId(), refreshToken,
                        toSeconds(refreshTokenProvider.getExpiredDate(refreshToken))
                )
        );

        return CustomJwts.of(accessToken, refreshToken);
    }

    public String createTemporaryToken(Member member) {
        return temporaryTokenProvider.createToken(TemporaryTokenClaim.of(member.getMemberId()));
    }

    private long toSeconds(LocalDateTime expiredDate) {
        return Duration.between(LocalDateTime.now(), expiredDate).getSeconds();
    }

    public RefreshToken refresh(
            Long memberId,
            String oldRefreshToken,
            String newRefreshToken
    ) throws IllegalArgumentException, IllegalStateException {
        RefreshToken refreshToken = loadRefreshTokenPort.findByMemberId(memberId).orElseThrow(
                ()-> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );
        validateToken(oldRefreshToken, refreshToken);

        refreshToken.rotation(newRefreshToken);
        commandRefreshTokenPort.save(refreshToken);

        return refreshToken;
    }

    private void validateToken(String oldRefreshToken, RefreshToken refreshToken) {
        if (isTakenAway(oldRefreshToken, refreshToken.getToken())) {
            commandRefreshTokenPort.delete(refreshToken);
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISMATCHED);
        }
    }

    private boolean isTakenAway(String requestRefreshToken, String expectedRefreshToken) {
        return !requestRefreshToken.equals(expectedRefreshToken);
    }

}
