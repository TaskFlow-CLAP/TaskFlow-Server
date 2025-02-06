package clap.server.application.port.outbound.email;

public interface SendEmailPort {

    void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword);

    void sendVerificationEmail(String memberEmail, String receiverName, String verificationCode);

}