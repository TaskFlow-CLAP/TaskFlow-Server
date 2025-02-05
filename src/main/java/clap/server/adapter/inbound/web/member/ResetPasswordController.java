package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.request.UpdateInitialPasswordRequest;
import clap.server.adapter.inbound.web.dto.member.request.UpdatePasswordRequest;
import clap.server.application.port.inbound.member.ResetInitialPasswordUsecase;
import clap.server.application.port.inbound.member.ResetPasswordUsecase;
import clap.server.application.port.inbound.member.VerifyPasswordUseCase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "01. Member [비밀번호 관련]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResetPasswordController {
    private final ResetPasswordUsecase resetPasswordUsecase;
    private final ResetInitialPasswordUsecase resetInitialPasswordUsecase;
    private final VerifyPasswordUseCase verifyPasswordUseCase;

    @Operation(summary = "초기 로그인 후 비밀번호 재설정 API")
    @PatchMapping("/members/initial-password")
    public void resetPasswordAndActivateMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody UpdateInitialPasswordRequest request) {
        resetInitialPasswordUsecase.resetPasswordAndActivateMember(userInfo.getUserId(),request.password());
    }

    @Operation(summary = "비밀번호 재설정 API")
    @PatchMapping("/members/password")
    public void resetPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody UpdatePasswordRequest request) {
        resetPasswordUsecase.resetPassword(userInfo.getUserId(), request.password());
    }


    @Operation(summary = "비밀번호 검증 API")
    @GetMapping("/members/password")
    public void verifyPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                              @RequestBody @NotBlank String password) {
        verifyPasswordUseCase.verifyPassword(userInfo.getUserId(), password);
    }
}
