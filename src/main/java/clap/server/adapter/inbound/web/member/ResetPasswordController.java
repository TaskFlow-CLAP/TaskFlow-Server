package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.auth.ResetPasswordUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "비밀번호 재설정")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResetPasswordController {
    private final ResetPasswordUsecase resetPasswordUsecase;

    @Operation(summary = "초기 로그인 후 비밀번호 재설정 API", description = "swagger에서 따옴표를 포함하지 않고 요청합니다.")
    @PatchMapping("/members/initial-password")
    public void resetPasswordAndActivateMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody @NotNull String password) {
        resetPasswordUsecase.resetPasswordAndActivateMember(userInfo.getUserId(), password);
    }

    @Operation(summary = "비밀번호 재설정 API", description = "swagger에서 따옴표를 포함하지 않고 요청합니다.")
    @PatchMapping("/members/password")
    public void resetPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody @Valid String password) {
        resetPasswordUsecase.resetPassword(userInfo.getUserId(), password);
    }

}
