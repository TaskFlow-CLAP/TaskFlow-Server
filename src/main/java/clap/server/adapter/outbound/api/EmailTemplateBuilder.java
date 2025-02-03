package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.EmailTemplate;
import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailTemplateBuilder {
    private final SpringTemplateEngine templateEngine;

    public EmailTemplate createWebhookTemplate(SendWebhookRequest request) {
        Context context = new Context();
        String templateName = "";
        String subject = "";
        switch (request.notificationType()) {
            case TASK_REQUESTED:
                templateName = "task-request";
                subject = "[TaskFlow 알림] 신규 작업이 요청되었습니다.";
                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());
                break;
            case STATUS_SWITCHED:
                templateName = "status-switched";
                subject = "[TaskFlow 알림] 작업 상태가 변경되었습니다.";
                context.setVariable("receiverName", request.senderName());
                context.setVariable("title", request.taskName());
                break;
            case PROCESSOR_CHANGED:
                templateName = "processor-changed";
                subject = "[TaskFlow 알림] 작업 담당자가 변경되었습니다.";
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case PROCESSOR_ASSIGNED:
                templateName = "processor-assigned";
                subject = "[TaskFlow 알림] 작업 담당자가 지정되었습니다.";
                context.setVariable("processorName", request.message());
                context.setVariable("title", request.taskName());
                break;
            case COMMENT:
                subject = "[TaskFlow 알림] 댓글이 작성되었습니다.";
                context.setVariable("comment", request.message());
                context.setVariable("title", request.taskName());
                break;

        }
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(request.email(), subject, body);
    }

    public EmailTemplate createInvitationTemplate(String receiver, String receiverName, String initialPassword) {
        Context context = new Context();
        String templateName = "invitation";
        String subject = "[TaskFlow 초대] 회원가입을 환영합니다.";
        context.setVariable("invitationLink", "https://example.com/reset-password"); //TODO:비밀번호 설정 링크로 변경 예정
        context.setVariable("initialPassword", initialPassword);
        context.setVariable("receiverName", receiverName);
        String body = templateEngine.process(templateName, context);
        return new EmailTemplate(receiver, subject, body);
    }
}
