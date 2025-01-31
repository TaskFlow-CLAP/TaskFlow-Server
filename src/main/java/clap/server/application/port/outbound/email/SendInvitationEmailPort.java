package clap.server.application.port.outbound.email;

import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;

public interface SendInvitationEmailPort {
    void sendInvitationEmail(SendInvitationRequest request, String memberEmail, String initialPassword);
}
