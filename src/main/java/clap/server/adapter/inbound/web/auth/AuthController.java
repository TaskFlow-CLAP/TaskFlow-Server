package clap.server.adapter.inbound.web.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginRequest;
import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.application.port.inbound.auth.AuthUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/auths")
@Slf4j
public class AuthController {
    private final AuthUsecase authUsecase;

    @Operation(description = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authUsecase.login(request.nickname(), request.password()));
    }

}
