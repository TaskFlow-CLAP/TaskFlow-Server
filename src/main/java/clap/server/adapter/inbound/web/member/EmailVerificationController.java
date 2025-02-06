package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.response.SendVerificationCodeRequest;
import clap.server.application.port.inbound.member.SendVerificationEmailUsecase;
import clap.server.application.port.inbound.member.VerifyEmailCodeUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "00. Auth [인증번호]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailVerificationController {
    private final SendVerificationEmailUsecase sendVerificationEmailUsecase;
    private final VerifyEmailCodeUsecase verifyEmailCodeUsecase;

    @Operation(summary = "인증번호 전송 API")
    @PostMapping("/verification/email")
    public void sendVerificationEmail(@RequestBody SendVerificationCodeRequest request) {
        sendVerificationEmailUsecase.sendVerificationCode(request);
    }

    @Operation(summary = "인증번호 검증 API")
    @PostMapping("/verification")
    public void sendVerificationEmail(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                      @RequestParam String code) {
        verifyEmailCodeUsecase.verifyEmailCode(userInfo.getUserId(), code);
    }
}
