package clap.server.application.service.notification;

import clap.server.adapter.outbound.api.dto.SendAgitRequest;
import clap.server.adapter.outbound.api.dto.SendKakaoWorkRequest;
import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@ApplicationService
@RequiredArgsConstructor
public class SendWebhookService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void sendWebhookNotification(Member receiver, NotificationType notificationType,
                                        Task task, String message, String commenterName) {
        // 공통 데이터
        String email = receiver.getMemberInfo().getEmail();
        String taskTitle = task.getTitle();
        String requesterNickname = task.getRequester().getNickname();

        // 이메일 전송
        if (receiver.getEmailNotificationEnabled()) {
            SendWebhookRequest sendWebhookRequest = new SendWebhookRequest(
                    email, notificationType, taskTitle, requesterNickname, message, commenterName
            );
            applicationEventPublisher.publishEvent(sendWebhookRequest);
        }

        // 카카오워크 전송
        if (receiver.getKakaoworkNotificationEnabled()) {
            SendKakaoWorkRequest sendKakaoWorkRequest = new SendKakaoWorkRequest(
                    email, notificationType, taskTitle, requesterNickname, message, commenterName
            );
            applicationEventPublisher.publishEvent(sendKakaoWorkRequest);
        }

        // 아지트 전송
        if (receiver.getAgitNotificationEnabled()) {
            SendAgitRequest sendAgitRequest = new SendAgitRequest(
                    email, notificationType, taskTitle, requesterNickname, message, commenterName
            );
            applicationEventPublisher.publishEvent(sendAgitRequest);
        }
    }
}
