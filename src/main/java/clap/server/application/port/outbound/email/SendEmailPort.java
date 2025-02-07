package clap.server.application.port.outbound.email;

public interface SendEmailPort {

    void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword, String userNickname);

    void sendVerificationEmail(String memberEmail, String receiverName, String verificationCode);

    void sendNewPasswordEmail(String memberEmail, String receiverName, String newPassword);

}