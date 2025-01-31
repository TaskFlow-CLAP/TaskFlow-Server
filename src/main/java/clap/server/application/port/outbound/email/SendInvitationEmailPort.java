package clap.server.application.port.outbound.email;

public interface SendInvitationEmailPort {
    void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword);
}
