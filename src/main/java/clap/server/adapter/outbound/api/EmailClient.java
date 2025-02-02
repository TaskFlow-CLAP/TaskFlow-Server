package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendEmailPort;
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
public class EmailClient implements SendEmailPort {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(SendWebhookRequest request) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String body;
            Context context = new Context();

            if (request.notificationType() == NotificationType.TASK_REQUESTED) {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 알림] 신규 작업이 요청되었습니다.");

                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());

                body = templateEngine.process("task-request", context);
            } else if (request.notificationType() == NotificationType.STATUS_SWITCHED) {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 알림] 작업 상태가 변경되었습니다.");

                context.setVariable("status", request.message());
                context.setVariable("title", request.taskName());

                body = templateEngine.process("status-switch", context);
            } else if (request.notificationType() == NotificationType.PROCESSOR_CHANGED) {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 알림] 작업 담당자가 변경되었습니다.");

                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());

                body = templateEngine.process("processor-change", context);
            } else if (request.notificationType() == NotificationType.PROCESSOR_ASSIGNED) {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 알림] 작업 담당자가 지정되었습니다.");

                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());

                body = templateEngine.process("processor-assign", context);
            } else if (request.notificationType() == NotificationType.INVITATION) {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 초대] 회원가입을 환영합니다.");

                context.setVariable("invitationLink", "https://example.com/reset-password"); //TODO:비밀번호 설정 링크로 변경 예정
                context.setVariable("initialPassword", request.message());
                context.setVariable("receiverName", request.senderName());

                body = templateEngine.process("invitation", context);
            } else {
                helper.setTo(request.email());
                helper.setSubject("[TaskFlow 알림] 댓글이 작성되었습니다.");

                context.setVariable("comment", request.message());
                context.setVariable("title", request.taskName());

                body = templateEngine.process("comment", context);
            }

            helper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(memberEmail);
            helper.setSubject("[TaskFlow 초대] 회원가입을 환영합니다.");

            Context context = new Context();
            context.setVariable("invitationLink", "https://example.com/reset-password"); // TODO: 비밀번호 재설정 링크로 변경
            context.setVariable("initialPassword", initialPassword);
            context.setVariable("receiverName", receiverName);

            String body = templateEngine.process("invitation", context);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException(NotificationErrorCode.EMAIL_SEND_FAILED);
        }
    }
}
