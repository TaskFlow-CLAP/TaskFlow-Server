package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.label.AddLabelRequest;

public interface AddLabelUsecase {

    void addLabel(Long adminId, AddLabelRequest request);


}
