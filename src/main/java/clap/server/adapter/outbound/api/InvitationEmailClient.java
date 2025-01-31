package clap.server.adapter.outbound.api;

import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;
import clap.server.application.port.outbound.email.SendInvitationEmailPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ExternalApiAdapter
@RequiredArgsConstructor
public class InvitationEmailClient implements SendInvitationEmailPort {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Override
    public void sendInvitationEmail(SendInvitationRequest request, String memberEmail, String initialPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 이메일 설정
            helper.setTo(memberEmail);
            helper.setSubject("[TaskFlow 초대] 회원가입을 환영합니다.");

            // Thymeleaf 컨텍스트 설정
            Context context = new Context();
            context.setVariable("invitationLink", "https://example.com/reset-password"); // TODO: 링크 변경 필요
            context.setVariable("initialPassword", initialPassword);
            context.setVariable("receiverName", "사용자 이름"); // 사용자 이름 필요

            // 이메일 템플릿 처리
            String body = templateEngine.process("invitation", context);
            helper.setText(body, true);

            // 이메일 전송
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }
}
