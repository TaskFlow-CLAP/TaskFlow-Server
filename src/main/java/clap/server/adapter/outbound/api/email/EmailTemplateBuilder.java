package clap.server.adapter.outbound.api.email;

import clap.server.adapter.outbound.api.data.EmailTemplate;
import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailTemplateBuilder {

    private final SpringTemplateEngine templateEngine;

    @Value("${redirect.url.login}")
    private String REDIRECT_URL_LOGIN;

    public EmailTemplate createWebhookTemplate(PushNotificationTemplate request, String taskDetailUrl) {
        Context context = new Context();
        String templateName = "";
        String subject = "";
        switch (request.notificationType()) {
            case TASK_REQUESTED:
                templateName = "task-request";
                subject = "[TaskFlow] 신규 작업 "+ request.taskName()+ "가 요청되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());
                break;
            case STATUS_SWITCHED:
                if (request.message().equals("TERMINATED")) {
                    templateName = "task-terminated";
                    subject = "[TaskFlow] " + request.taskName() + " 작업이 종료되었습니다.";
                    context.setVariable("taskDetailUrl", taskDetailUrl);
                    context.setVariable("reason", request.reason());
                    context.setVariable("title", request.taskName());
                } else {
                    templateName = "status-switched";
                    subject = "[TaskFlow] "+ request.taskName()+ " 작업의 상태가 " + request.message() + "으로 변경되었습니다.";
                    context.setVariable("taskDetailUrl", taskDetailUrl);
                    context.setVariable("receiverName", request.senderName());
                    context.setVariable("title", request.taskName());
                    context.setVariable("status", request.message());
                }
                break;
            case PROCESSOR_CHANGED:
                templateName = "processor-changed";
                subject = "[TaskFlow] "+ request.taskName()+ " 작업의 담당자가 " + request.message() + "으로 변경되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case PROCESSOR_ASSIGNED:
                templateName = "processor-assigned";
                subject = "[TaskFlow] "+ request.taskName()+ " 작업의 담당자가 " + request.message() + "으로 선정되었습니다..";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case COMMENT:
                templateName = "comment";
                subject = "[TaskFlow] " + request.taskName() + " 작업에 " + request.commenterName() + "님이 댓글을 작성하였습니다";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("commenter", request.commenterName());
                context.setVariable("comment", request.message());
                context.setVariable("title", request.taskName());
                break;

        }
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(request.email(), subject, body);
    }

    public EmailTemplate createInvitationTemplate(String receiver, String receiverName,
                                                  String initialPassword, String userNickname) {
        Context context = new Context();
        String templateName = "invitation";
        String subject = "[TaskFlow 초대] 회원가입을 환영합니다.";
        context.setVariable("userNickname", userNickname);
        context.setVariable("invitationLink", REDIRECT_URL_LOGIN);
        context.setVariable("initialPassword", initialPassword);
        context.setVariable("receiverName", receiverName);
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(receiver, subject, body);
    }

    public EmailTemplate createVerificationCodeTemplate(String receiver, String receiverName, String verificationCode) {
        Context context = new Context();
        String templateName = "verification";
        String subject = "[TaskFlow] 비밀번호 재설정 인증 번호";
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("receiverName", receiverName);
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(receiver, subject, body);
    }

    public EmailTemplate createNewPasswordTemplate(String receiver, String receiverName, String newPassword) {
        Context context = new Context();
        String templateName = "new-password";
        String subject = "[TaskFlow] 비밀번호 재설정";
        context.setVariable("loginLink", "http://localhost:5173/login");
        context.setVariable("newPassword", newPassword);
        context.setVariable("receiverName", receiverName);
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(receiver, subject, body);
    }
}