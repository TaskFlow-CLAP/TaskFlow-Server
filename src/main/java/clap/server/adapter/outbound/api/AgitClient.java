package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@ExternalApiAdapter
@RequiredArgsConstructor
public class AgitClient implements SendAgitPort {

    @Value("${webhook.agit.url}")
    private String AGIT_WEBHOOK_URL;

    private final CommandTaskPort commandTaskPort;

    @Override
    public void sendAgit(PushNotificationTemplate request, Task task) {

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(getPayLoad(request, task), headers);

        RestTemplate restTemplate = new RestTemplate();


        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
            commandTaskPort.updateAgitPostId(responseEntity, task);
        }
        else {
            restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
        }
    }

    private String getPayLoad(PushNotificationTemplate request, Task task) {

        String payload;
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            payload = "{"
                    + "\"text\": \"" + getMessage(request) + "\","
                    + "\"mrkdwn\": true" + "}";
        }

        else {
            payload = "{"
                    + "\"parent_id\": " + task.getAgitPostId() + ","
                    + "\"text\": \"" + getMessage(request) + "\","
                    + "\"mrkdwn\": true"
                    + "}";
        }
        return payload;
    }

    private String getMessage(PushNotificationTemplate request) {
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
