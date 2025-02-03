package clap.server.adapter.outbound.api;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import clap.server.application.port.outbound.webhook.SendKaKaoWorkPort;
import clap.server.common.annotation.architecture.ExternalApiAdapter;
import clap.server.exception.AdapterException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@ExternalApiAdapter
@RequiredArgsConstructor
public class KakaoWorkClient implements SendKaKaoWorkPort {

    @Value("${webhook.kakaowork.url}")
    private String kakaworkUrl;

    @Value("${webhook.kakaowork.auth}")
    private String kakaworkAuth;

    private final ObjectBlockBuilder objectBlockBuilder;

    @Override
    public void sendKakaoWork(SendWebhookRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Payload 생성
        String payload = objectBlockBuilder.makeObjectBlock(request);

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", kakaworkAuth);

        // HTTP 요청 엔터티 생성
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            // Post 요청 전송
            restTemplate.exchange(
                    kakaworkUrl, HttpMethod.POST, entity, String.class
            );

        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.KAKAO_SEND_FAILED);
        }
    }
}
