package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.request.SendInitialPasswordRequest;
import clap.server.adapter.inbound.web.dto.member.request.UpdateInitialPasswordRequest;
import clap.server.adapter.inbound.web.dto.member.request.UpdatePasswordRequest;
import clap.server.adapter.inbound.web.dto.member.request.VerifyPasswordRequest;
import clap.server.application.port.inbound.member.ResetInitialPasswordUsecase;
import clap.server.application.port.inbound.member.ResetPasswordUsecase;
import clap.server.application.port.inbound.member.SendNewPasswordUsecase;
import clap.server.application.port.inbound.member.VerifyPasswordUseCase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "01. Member [비밀번호 관련]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResetPasswordController {
    private final ResetPasswordUsecase resetPasswordUsecase;
    private final ResetInitialPasswordUsecase resetInitialPasswordUsecase;
    private final VerifyPasswordUseCase verifyPasswordUseCase;
    private final SendNewPasswordUsecase sendNewPasswordUsecase;

    @Operation(summary = "초기 로그인 후 비밀번호 재설정 API")
    @PatchMapping("/members/initial-password")
    public void resetPasswordAndActivateMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody  @Valid UpdateInitialPasswordRequest request) {
        resetInitialPasswordUsecase.resetPasswordAndActivateMember(userInfo.getUserId(),request.password());
    }

    @Operation(summary = "비밀번호 재설정 API")
    @PatchMapping("/members/password")
    public void resetPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody @Valid UpdatePasswordRequest request) {
        resetPasswordUsecase.resetPassword(userInfo.getUserId(), request.password());
    }


    @Operation(summary = "비밀번호 검증 API")
    @PostMapping("/members/password")
    public void verifyPassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                              @RequestBody @Valid VerifyPasswordRequest request) {
        verifyPasswordUseCase.verifyPassword(userInfo.getUserId(), request.password());
    }

    @Operation(summary = "비밀번호 재설정 이메일 전송 API")
    @PostMapping("/new-password")
    public void sendNewPasswordEmail(@RequestBody @Valid SendInitialPasswordRequest request) {
        sendNewPasswordUsecase.sendInitialPassword(request);
    }
}
