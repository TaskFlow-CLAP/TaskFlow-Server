package clap.server.adapter.outbound.api.agit;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.domain.model.task.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AgitTemplateBuilder {

    public HttpEntity<String> createAgitEntity(PushNotificationTemplate request, Task task, String taskDetailUrl) {
        return new HttpEntity<>(createPayLoad(request, task, taskDetailUrl), createHeaders());
    }


    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    public String createPayLoad(PushNotificationTemplate request, Task task, String taskDetailUrl) {

        String payload;
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            payload = "{"
                    + "\"text\": \"" + createMessage(request, taskDetailUrl) + "\","
                    + "\"mrkdwn\": true" + "}";
        }

        else {
            payload = "{"
                    + "\"parent_id\": " + task.getAgitPostId() + ","
                    + "\"text\": \"" + createMessage(request, taskDetailUrl) + "\","
                    + "\"mrkdwn\": true"
                    + "}";
        }
        return payload;
    }

    public String createMessage(PushNotificationTemplate request, String taskDetailUrl) {

        return switch (request.notificationType()) {
            case TASK_REQUESTED -> "📌 *새 작업이 요청되었습니다.*\\n"
                    + "\\t\\t*• 🔖  작업명:*  " + "*" + request.taskName() + "*" + "\\n"
                    + "\\t\\t*• 🙋  요청자:*  " + "*" + request.senderName() + "*" + "\\n\\n"
                    + "\\t[자세히 보기](" + taskDetailUrl + ")";

            case STATUS_SWITCHED -> "작업 상태가 " + "*" + request.message() + "*" + "으로 변경되었습니다.";

            case PROCESSOR_CHANGED -> "담당자가 " + "*" + request.message() + "*" + "으로 변경되었습니다.";

            case PROCESSOR_ASSIGNED -> "*작업*이 *승인*되었습니다.\\n"
                    + "\\t\\t*•담당자:* " + "*" + request.message() + "*";

            default -> null;
        };
    }
}
