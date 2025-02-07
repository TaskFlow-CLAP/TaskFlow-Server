package clap.server.application.service.auth;

import clap.server.adapter.inbound.web.dto.auth.response.ReissueTokenResponse;
import clap.server.application.port.inbound.auth.ReissueTokenUsecase;
import clap.server.application.port.outbound.auth.refresh.CommandRefreshTokenPort;
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
    private final ManageTokenService manageTokenService;
    private final CommandRefreshTokenPort commandRefreshTokenPort;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public ReissueTokenResponse reissueToken(String oldRefreshToken) {
        Long memberId = manageTokenService.resolveRefreshToken(oldRefreshToken);
        RefreshToken newRefreshToken;
        try {
            newRefreshToken = refresh(memberId, oldRefreshToken,
                    manageTokenService.issueRefreshToken(memberId).getToken());
        } catch (IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (IllegalStateException e) {
            throw new AuthException(AuthErrorCode.TAKEN_AWAY_TOKEN);
        }

        String newAccessToken = manageTokenService.issueAccessToken(memberId);
        CustomJwts tokens = CustomJwts.of(newAccessToken, newRefreshToken.getToken());
        return toReissueTokenResponse(tokens);
    }

    private RefreshToken refresh(
            Long memberId,
            String oldRefreshToken,
            String newRefreshToken
    ) throws IllegalArgumentException, IllegalStateException {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(memberId);
        refreshTokenService.validateToken(oldRefreshToken, refreshToken);

        refreshToken.rotation(newRefreshToken);
        commandRefreshTokenPort.save(refreshToken);
        return refreshToken;
    }

}
