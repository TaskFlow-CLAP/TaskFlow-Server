package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.label.AddAndEditLabelRequest;

public interface UpdateLabelUsecase {
    void editLabel(Long adminId, Long labelId, AddAndEditLabelRequest request);
}
