package clap.server.application.service.auth;

import clap.server.application.port.outbound.auth.CommandRefreshTokenPort;
import clap.server.application.port.outbound.auth.LoadRefreshTokenPort;
import clap.server.domain.model.auth.RefreshToken;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static clap.server.domain.model.auth.RefreshToken.isTakenAway;

@RequiredArgsConstructor
@Component
@Slf4j
public class RefreshTokenService {
    private final LoadRefreshTokenPort loadRefreshTokenPort;
    private final CommandRefreshTokenPort commandRefreshTokenPort;

    public RefreshToken getRefreshToken(Long memberId) {
        return loadRefreshTokenPort.findByMemberId(memberId).orElseThrow(
                () -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );
    }

    public void saveRefreshToken(RefreshToken token) {
        commandRefreshTokenPort.save(token);
    }

    public void deleteRefreshToken(RefreshToken token) {
        commandRefreshTokenPort.delete(token);
    }

    public void validateToken(String oldRefreshToken, RefreshToken refreshToken) {
        if (isTakenAway(oldRefreshToken, refreshToken.getToken())) {
            commandRefreshTokenPort.delete(refreshToken);
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISMATCHED);
        }
    }


}
