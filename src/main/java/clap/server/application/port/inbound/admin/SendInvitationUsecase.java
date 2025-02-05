package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.request.SendInvitationRequest;

public interface SendInvitationUsecase {
    void sendInvitation(SendInvitationRequest request);
}
