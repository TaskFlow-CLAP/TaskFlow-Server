package clap.server.adapter.inbound.web.auth;

import clap.server.adapter.inbound.web.dto.member.request.SendVerificationCodeRequest;
import clap.server.adapter.inbound.web.dto.member.request.VerifyCodeRequest;
import clap.server.application.port.inbound.member.SendVerificationEmailUsecase;
import clap.server.application.port.inbound.member.VerifyEmailCodeUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "00. Auth [인증번호]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailVerificationController {
    private final SendVerificationEmailUsecase sendVerificationEmailUsecase;
    private final VerifyEmailCodeUsecase verifyEmailCodeUsecase;

    @Deprecated
    @Operation(summary = "인증번호 전송 API")
    @PostMapping("/verifications/email")
    public void sendVerificationEmail(@RequestBody SendVerificationCodeRequest request) {
        sendVerificationEmailUsecase.sendVerificationCode(request);
    }

    @Deprecated
    @Operation(summary = "인증번호 검증 API")
    @PostMapping("/verifications")
    public void sendVerificationEmail(@RequestBody VerifyCodeRequest request) {
        verifyEmailCodeUsecase.verifyEmailCode(request);
    }
}
