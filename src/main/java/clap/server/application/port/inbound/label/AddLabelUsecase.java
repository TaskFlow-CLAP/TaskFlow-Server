package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.label.CreateLabelRequest;

public interface AddLabelUsecase {

    void addLabel(Long adminId, CreateLabelRequest request);


}
