package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.application.service.task.UpdateTaskService;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
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
    private final TaskService taskService;

    @Override
    public void sendAgit(PushNotificationTemplate request, Task task) {

        HttpEntity<String> entity = agitTemplateBuilder.createAgitEntity(request, task);

        RestTemplate restTemplate = new RestTemplate();
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
            updateAgitPostId(responseEntity, task);
        }
        else {
            restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
        }
    }

    private void updateAgitPostId(ResponseEntity<String> responseEntity, Task task) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            task.updateAgitPostId(jsonNode.get("id").asLong());
            taskService.upsert(task);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(NotificationErrorCode.AGIT_SEND_FAILED);
        }
    }
}
