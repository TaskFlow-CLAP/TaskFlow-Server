package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.EmailTemplate;
import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
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
                subject = "[TaskFlow] 신규 작업"+ request.taskName()+ "요청되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());
                break;
            case STATUS_SWITCHED:
                templateName = "status-switched";
                subject = "[TaskFlow] "+ request.taskName()+ " " + request.message()+ "으로 변경되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());
                context.setVariable("status", request.message());
                break;
            case PROCESSOR_CHANGED:
                templateName = "processor-changed";
                subject = "[TaskFlow] "+ request.taskName()+ "담당자" + request.message() + " 변경되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case PROCESSOR_ASSIGNED:
                templateName = "processor-assigned";
                subject = "[TaskFlow] "+ request.taskName()+ "담당자" + request.message() + " 지정되었습니다..";
                context.setVariable("taskDetailUrl", taskDetailUrl);
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case COMMENT:
                templateName = "comment";
                subject = "[TaskFlow 알림] 댓글이 작성되었습니다.";
                context.setVariable("taskDetailUrl", taskDetailUrl);
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
}