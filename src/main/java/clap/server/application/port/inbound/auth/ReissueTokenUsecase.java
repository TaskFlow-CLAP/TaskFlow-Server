package clap.server.application.port.inbound.auth;

import clap.server.adapter.inbound.web.dto.auth.response.ReissueTokenResponse;

public interface ReissueTokenUsecase {
    ReissueTokenResponse reissueToken(String oldRefreshToken);
}
