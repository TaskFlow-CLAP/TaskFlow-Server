package clap.server.adapter.outbound.api;

import clap.server.adapter.inbound.web.dto.webhook.SendAgitRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


@PersistenceAdapter
@RequiredArgsConstructor
public class AgitClient implements SendAgitPort {

    private static final String AGITWEBHOOK_URL = "https://agit.io/webhook/a342181d-fb18-4eb0-a99a-30f4fb5b14b1";

    @Override
    public void sendAgit(SendAgitRequest request) {
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
        restTemplate.exchange(AGITWEBHOOK_URL, HttpMethod.POST, entity, String.class);
    }
}
