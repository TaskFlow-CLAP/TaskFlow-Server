package clap.server.adapter.inbound.web.auth;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.auth.LoginRequest;
import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.auth.LoginUsecase;
import clap.server.application.port.inbound.auth.LogoutUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.config.annotation.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static clap.server.common.utils.ClientIpParseUtil.getClientIp;

@Slf4j
@Tag(name = "00. Auth", description = "로그인, 로그아웃, 토큰 재발급 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {
    private final LoginUsecase loginUsecase;
    private final LogoutUsecase logoutUsecase;

    @LogType(LogStatus.LOGIN)
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestHeader(name = "sessionId") String sessionId,
                                               @RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        LoginResponse response = loginUsecase.login(request.nickname(), request.password(), sessionId, clientIp);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃 API")
    @DeleteMapping("/logout")
    public void logout(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                       @Parameter(hidden = true) @RequestHeader(value = "Authorization") String authHeader,
                                       @RequestHeader(value = "refreshToken") String refreshToken) {
        String accessToken = authHeader.split(" ")[1];
        logoutUsecase.logout(userInfo.getUserId(), accessToken, refreshToken);
    }

}
