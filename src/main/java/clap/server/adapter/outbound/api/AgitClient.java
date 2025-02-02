package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
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
    public void sendAgit(SendWebhookRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        String message = null;
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            message = request.taskName() + " 작업이 요청되었습니다.";
        }
        else if (request.notificationType() == NotificationType.COMMENT) {
            message = request.taskName() + " 작업에 " + request.commenterName() + "님이 댓글을 남기셨습니다.";
        }
        else if (request.notificationType() == NotificationType.PROCESSOR_ASSIGNED) {
            message = request.taskName() + " 작업에 담당자(" + request.message() + ")가 배정되었습니다.";
        }
        else if (request.notificationType() == NotificationType.PROCESSOR_CHANGED) {
            message = request.taskName() + " 작업의 담당자가 " + request.message() + "로 변경되었습니다.";
        }
        else {
            message = request.taskName() + " 작업의 상태가 "  + request.message() + "로 변경되었습니다";
        }

        String payload = "{\"text\":\"" + message + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // Post 요청
        restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
    }
}
