package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendKaKaoWorkPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@PersistenceAdapter
@RequiredArgsConstructor
public class KakaoWorkPersistenceAdapter implements SendKaKaoWorkPort {

    private static final String KAKAOWORK_URL = "https://api.kakaowork.com/v1/messages.send_by_email";
    private static final String KAKAOWORK_AUTH = "Bearer 1b01becc.a7f10da76d2e4038948771107cfe5c1d";

    private final ObjectBlockPersistenceAdapter makeObjectBlock;

    @Override
    public void sendKakaoWord(SendKakaoWorkRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Payload 생성
        String payload = null;
        if (request.notificationType() == NotificationType.TASK_REQUESTED) {
            payload = makeObjectBlock.makeTaskRequestBlock(request);
        }
        else if (request.notificationType() == NotificationType.PROCESSOR_ASSIGNED) {
            payload = makeObjectBlock.makeNewProcessorBlock(request);
        }
        else if (request.notificationType() == NotificationType.PROCESSOR_CHANGED) {
            payload = makeObjectBlock.makeProcessorChangeBlock(request);
        }
        else if (request.notificationType() == NotificationType.STATUS_SWITCHED) {
            payload = makeObjectBlock.makeTaskStatusBlock(request);
        }
        else {
            payload = makeObjectBlock.makeCommentBlock(request);
        }

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", KAKAOWORK_AUTH);

        // HTTP 요청 엔터티 생성
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            // Post 요청 전송
            restTemplate.exchange(
                    KAKAOWORK_URL, HttpMethod.POST, entity, String.class
            );

        } catch (Exception e) {
            throw new ApplicationException(NotificationErrorCode.KAKAO_SEND_FAILED);
        }
    }
}
