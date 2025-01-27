package clap.server.adapter.inbound.web.auth;

import clap.server.adapter.inbound.web.dto.auth.ReissueTokenResponse;
import clap.server.application.port.inbound.auth.ReissueTokenUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "00. Auth", description = "토큰 재발급 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class ReissueTokenController {
    private final ReissueTokenUsecase reissueTokenUsecase;

    @Operation(summary = "토큰 재발급 API")
    @PostMapping("/reissuance")
    public ResponseEntity<ReissueTokenResponse> login(@RequestHeader String refreshToken) {
        return ResponseEntity.ok(reissueTokenUsecase.reissueToken(refreshToken));
    }
}
