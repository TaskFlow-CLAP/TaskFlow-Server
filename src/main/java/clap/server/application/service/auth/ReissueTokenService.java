package clap.server.application.service.auth;

import clap.server.adapter.inbound.web.dto.auth.ReissueTokenResponse;
import clap.server.application.port.inbound.auth.ReissueTokenUsecase;
import clap.server.application.port.outbound.auth.CommandRefreshTokenPort;
import clap.server.application.port.outbound.auth.LoadRefreshTokenPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.auth.RefreshToken;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.response.AuthResponseMapper.toReissueTokenResponse;

@ApplicationService
@RequiredArgsConstructor
class ReissueTokenService implements ReissueTokenUsecase {
    private final IssueTokenService issueTokenService;
    private final LoadRefreshTokenPort loadRefreshTokenPort;
    private final CommandRefreshTokenPort commandRefreshTokenPort;

    @Transactional
    public ReissueTokenResponse reissueToken(String oldRefreshToken) {
        Long memberId = issueTokenService.resolveRefreshToken(oldRefreshToken);
        RefreshToken newRefreshToken;
        try {
            newRefreshToken = refresh(memberId, oldRefreshToken,
                    issueTokenService.issueRefreshToken(memberId).getToken());
        } catch (IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (IllegalStateException e) {
            throw new AuthException(AuthErrorCode.TAKEN_AWAY_TOKEN);
        }

        String newAccessToken = issueTokenService.issueAccessToken(memberId);
        CustomJwts tokens = CustomJwts.of(newAccessToken, newRefreshToken.getToken());
        return toReissueTokenResponse(tokens);
    }

    private RefreshToken refresh(
            Long memberId,
            String oldRefreshToken,
            String newRefreshToken
    ) throws IllegalArgumentException, IllegalStateException {
        RefreshToken refreshToken = loadRefreshTokenPort.findByMemberId(memberId).orElseThrow(
                () -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
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
