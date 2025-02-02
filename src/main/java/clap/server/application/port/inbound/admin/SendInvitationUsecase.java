package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;

public interface SendInvitationUsecase {
    void sendInvitation(SendInvitationRequest request);
}
