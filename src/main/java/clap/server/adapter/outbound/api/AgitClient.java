package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.application.service.task.UpdateTaskService;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.domain.model.task.Task;
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

    private final UpdateTaskService updateTaskService;
    private final AgitTemplateBuilder agitTemplateBuilder;

    @Override
    public void sendAgit(PushNotificationTemplate request, Task task) {

        HttpEntity<String> entity = agitTemplateBuilder.createAgitEntity(request, task);

        RestTemplate restTemplate = new RestTemplate();
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
            updateTaskService.updateAgitPostId(responseEntity, task);
        }
        else {
            restTemplate.exchange(AGIT_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
        }
    }
}
