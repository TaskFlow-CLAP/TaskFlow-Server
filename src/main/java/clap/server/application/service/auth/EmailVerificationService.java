package clap.server.application.service.auth;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.SendVerificationEmailUsecase;
import clap.server.application.port.inbound.member.VerifyEmailCodeUsecase;
import clap.server.application.port.outbound.auth.otp.CommandOtpPort;
import clap.server.application.port.outbound.auth.otp.LoadOtpPort;
import clap.server.application.port.outbound.email.SendEmailPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.utils.VerificationCodeGenerator;
import clap.server.domain.model.auth.Otp;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class EmailVerificationService implements SendVerificationEmailUsecase, VerifyEmailCodeUsecase {
    private final MemberService memberService;
    private final SendEmailPort sendEmailPort;
    private final CommandOtpPort commandOtpPort;
    private final LoadOtpPort loadOtpPort;

    @Override
    @Transactional
    public void sendVerificationCode(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        String verificationCode = VerificationCodeGenerator.generateRandomCode();
        commandOtpPort.save(new Otp(member.getMemberInfo().getEmail(), verificationCode));
        sendEmailPort.sendVerificationEmail(member.getMemberInfo().getEmail(), member.getNickname(), verificationCode);
    }


    @Override
    @Transactional
    public void verifyEmailCode(Long memberId, String code) {
        Member member = memberService.findActiveMember(memberId);
        Otp otp = loadOtpPort.findByEmail(member.getMemberInfo().getEmail()).orElseThrow(
                () -> new ApplicationException(AuthErrorCode.VERIFICATION_CODE_EXPIRED)
        );

        if(!code.equals(otp.code())){
            throw new ApplicationException(AuthErrorCode.VERIFICATION_CODE_MISMATCH);
        }
        else {
            commandOtpPort.deleteByEmail(member.getMemberInfo().getEmail());
        }
    }
}
