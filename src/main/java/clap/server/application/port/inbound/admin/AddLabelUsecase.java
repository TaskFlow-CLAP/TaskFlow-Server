package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.label.AddAndEditLabelRequest;

public interface AddLabelUsecase {

    void addLabel(Long adminId, AddAndEditLabelRequest request);


}
