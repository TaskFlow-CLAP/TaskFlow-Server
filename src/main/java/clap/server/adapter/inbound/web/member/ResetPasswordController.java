package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.member.ResetPasswordUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.validation.password.ValidPassword;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "01. Member", description = "비밀번호 재설정 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResetPasswordController {
    private final ResetPasswordUsecase resetPasswordUsecase;

    @Operation(summary = "초기 로그인 후 비밀번호 재설정 API", description = "swagger에서 따옴표를 포함하지 않고 요청합니다.")
    @PatchMapping("/members/initial-password")
    public void resetPasswordAndActivateMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody @NotBlank @ValidPassword String password) {
        resetPasswordUsecase.resetPasswordAndActivateMember(userInfo.getUserId(), password);
    }

    @Operation(summary = "비밀번호 재설정 API", description = "swagger에서 따옴표를 포함하지 않고 요청합니다.")
    @PatchMapping("/members/password")
    public void resetPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody @NotBlank @ValidPassword String password) {
        resetPasswordUsecase.resetPassword(userInfo.getUserId(), password);
    }

}
