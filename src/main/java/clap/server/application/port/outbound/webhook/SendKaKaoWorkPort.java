package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.SendKakaoWorkRequest;

public interface SendKaKaoWorkPort {

    void sendKakaoWord(SendKakaoWorkRequest request);
}
