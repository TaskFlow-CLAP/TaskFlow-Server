package clap.server.adapter.inbound.web.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginRequest;
import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.application.port.inbound.auth.AuthUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static clap.server.common.utils.ClientIpParseUtil.getClientIp;

@Slf4j
@Tag(name = "로그인 / 로그아웃")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {
    private final AuthUsecase authUsecase;

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestHeader(name = "sessionId") String sessionId,
                                               @RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        LoginResponse response = authUsecase.login(request.nickname(), request.password(), sessionId, clientIp);
        return ResponseEntity.ok(response);
    }


}
