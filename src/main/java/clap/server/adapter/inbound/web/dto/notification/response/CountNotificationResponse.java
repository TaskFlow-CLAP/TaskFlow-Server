package clap.server.adapter.inbound.web.dto.notification.response;

public record CountNotificationResponse(
        Long memberId,
        Integer count
) {
}
