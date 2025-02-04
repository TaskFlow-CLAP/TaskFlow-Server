package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.domain.model.task.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AgitTemplateBuilder {

    public HttpEntity<String> createAgitEntity(PushNotificationTemplate request, Task task) {
        return new HttpEntity<>(createPayLoad(request, task), createHeaders());
    }


    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    public String createPayLoad(PushNotificationTemplate request, Task task) {

        String payload;
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            payload = "{"
                    + "\"text\": \"" + createMessage(request) + "\","
                    + "\"mrkdwn\": true" + "}";
        }

        else {
            payload = "{"
                    + "\"parent_id\": " + task.getAgitPostId() + ","
                    + "\"text\": \"" + createMessage(request) + "\","
                    + "\"mrkdwn\": true"
                    + "}";
        }
        return payload;
    }

    public String createMessage(PushNotificationTemplate request) {
        String taskUrl = "https://www.naver.com"; //Todo 작업 상세페이지 url 추가

        return  switch (request.notificationType()) {
            case TASK_REQUESTED -> "📌 *새 작업 요청:* `" + request.taskName() + "`\\n"
                    + "\\t\\t*•요청자: " + request.senderName() + "*\\n"
                    + "\\t\\t[OPEN](" + taskUrl + ")";
            case STATUS_SWITCHED -> "⚙️ *작업 상태 변경:* `" + request.taskName() + "\\n"
                    + "\\t\\t*•작업 상태: " + request.message() + "*\\n"
                    + "\\t\\t[OPEN](" + taskUrl + ")";
            case PROCESSOR_CHANGED -> "🔄 *담당자 변경:* `" + request.taskName() + "\\n"
                    + "\\t\\t*•새 담당자: " + request.message() + "*\\n"
                    + "\\t\\t[OPEN](" + taskUrl + ")";
            case PROCESSOR_ASSIGNED -> "👤 *작업 담당자 배정:* `" + request.taskName() + "\\n"
                    + "\\t\\t*•담당자: " + request.message() + "*\\n"
                    + "\\t\\t[OPEN](" + taskUrl + ")";
            default -> null;
        };
    }
}
