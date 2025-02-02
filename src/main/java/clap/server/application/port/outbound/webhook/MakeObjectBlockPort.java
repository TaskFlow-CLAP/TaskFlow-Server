package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;

public interface MakeObjectBlockPort {

    String makeTaskRequestBlock(SendWebhookRequest request);

    String makeNewProcessorBlock(SendWebhookRequest request);

    String makeProcessorChangeBlock(SendWebhookRequest request);

    String makeCommentBlock(SendWebhookRequest request);

    String makeTaskStatusBlock(SendWebhookRequest request);
}
