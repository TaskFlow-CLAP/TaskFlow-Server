package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


@ExternalApiAdapter
@RequiredArgsConstructor
public class AgitClient implements SendAgitPort {

    @Value("${webhook.agit.url}")
    private String AGIT_WEBHOOK_URL;

    @Override
    public void sendAgit(PushNotificationTemplate request) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        RestTemplate restTemplate = new RestTemplate();
        String taskUrl = "https://210.109.59.9/api/tasks/" + request.taskId();

        String message = switch (request.notificationType()) {
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
            case COMMENT -> "💬 *새 댓글:* `" + request.taskName() + "`\\n"
                    + "\\t\\t*•작성자: " + request.commenterName() + "\\n"
                    + "\\t\\t*•댓글 내용: " + request.message() + "\\n"
                    + "\\t\\t[OPEN](" + taskUrl + ")";
            default -> null;
        };

        String payload = "{"
                + "\"text\": \"" + message + "\","
                + "\"mrkdwn\": true" + "}";

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
    }
}
