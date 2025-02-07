package clap.server.adapter.outbound.api.agit;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.domain.model.task.Task;
import clap.server.exception.AdapterException;
import clap.server.exception.code.NotificationErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@ExternalApiAdapter
@RequiredArgsConstructor
public class AgitClient implements SendAgitPort {

    @Value("${webhook.agit.url}")
    private String AGIT_WEBHOOK_URL;

    private final AgitTemplateBuilder agitTemplateBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public Long sendAgit(PushNotificationTemplate request, Task task, String taskDetailUrl) {

        HttpEntity<String> entity = agitTemplateBuilder.createAgitEntity(request, task, taskDetailUrl);

        RestTemplate restTemplate = new RestTemplate();
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
            return getAgitPostId(responseEntity);
        }
        else {
            restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
            return null;
        }
    }

    private Long getAgitPostId(ResponseEntity<String> responseEntity) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            return jsonNode.get("id").asLong();
        } catch (JsonProcessingException e) {
            throw new AdapterException(NotificationErrorCode.AGIT_SEND_FAILED);
        }
    }
}
