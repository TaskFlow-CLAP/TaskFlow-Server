package clap.server.adapter.inbound.web.dto.notification;

public record CountNotificationResponse(
        Long memberId,
        Integer count
) {
}
