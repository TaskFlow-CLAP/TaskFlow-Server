package clap.server.application.service.member;

import clap.server.application.port.outbound.email.SendEmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendPasswordEmailService {
    private final SendEmailPort sendEmailPort;

    @Async("emailExecutor")
    public void sendNewPasswordEmail(String email, String name, String newPassword) {
        CompletableFuture.runAsync(() -> {
            try {
                sendEmailPort.sendNewPasswordEmail(email, name, newPassword);
            } catch (Exception e) {
                log.error("Failed to send new password email to: {}", email, e);
            }
        }).exceptionally(ex -> {
            log.error("Unexpected error occurred while sending new password email", ex);
            return null;
        });
    }
}
