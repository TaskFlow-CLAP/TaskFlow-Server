package clap.server.application.service.admin;

import clap.server.application.port.outbound.email.SendEmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
@Slf4j
@Service
@RequiredArgsConstructor
public class SendInvitationEmailService {
    private final SendEmailPort sendEmailPort;

    @Async("emailExecutor")
    public void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword, String userNickname) {
        CompletableFuture.runAsync(() -> {
            try {
                sendEmailPort.sendInvitationEmail(memberEmail, receiverName, initialPassword, userNickname);
            } catch (Exception e) {
                log.error("Failed to send new password email to: {}", memberEmail, e);
            }
        }).exceptionally(ex -> {
            log.error("Unexpected error occurred while sending new password email", ex);
            return null;
        });
    }
}