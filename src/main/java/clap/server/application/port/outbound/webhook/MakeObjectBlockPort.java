package clap.server.application.port.outbound.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;

public interface MakeObjectBlockPort {

    String makeTaskRequestBlock(SendKakaoWorkRequest request);

    String makeNewProcessorBlock(SendKakaoWorkRequest request);

    String makeProcessorChangeBlock(SendKakaoWorkRequest request);

    String makeCommentBlock(SendKakaoWorkRequest request);

    String makeTaskStatusBlock(SendKakaoWorkRequest request);
}
