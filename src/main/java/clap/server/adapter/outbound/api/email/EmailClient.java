package clap.server.adapter.outbound.api.email;

import clap.server.adapter.outbound.api.data.EmailTemplate;
import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import clap.server.application.port.outbound.email.SendEmailPort;
import clap.server.application.port.outbound.webhook.SendWebhookEmailPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.exception.AdapterException;
import clap.server.exception.code.NotificationErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@ExternalApiAdapter
@RequiredArgsConstructor
public class EmailClient implements SendEmailPort, SendWebhookEmailPort {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final JavaMailSender mailSender;

    @Override
    public void sendWebhookEmail(PushNotificationTemplate request, String taskDetailUrl) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            EmailTemplate template = emailTemplateBuilder.createWebhookTemplate(request, taskDetailUrl);
            helper.setTo(template.email());
            helper.setSubject(template.subject());
            helper.setText(template.body(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword, String userNickname) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            EmailTemplate template = emailTemplateBuilder.createInvitationTemplate(memberEmail, receiverName, initialPassword, userNickname);
            helper.setTo(template.email());
            helper.setSubject(template.subject());
            helper.setText(template.body(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void sendVerificationEmail(String memberEmail, String receiverName, String verificationCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            EmailTemplate template = emailTemplateBuilder.createVerificationCodeTemplate(memberEmail, receiverName, verificationCode);
            helper.setTo(template.email());
            helper.setSubject(template.subject());
            helper.setText(template.body(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void sendNewPasswordEmail(String memberEmail, String receiverName, String newPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            EmailTemplate template = emailTemplateBuilder.createNewPasswordTemplate(memberEmail, receiverName, newPassword);
            helper.setTo(template.email());
            helper.setSubject(template.subject());
            helper.setText(template.body(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }


}