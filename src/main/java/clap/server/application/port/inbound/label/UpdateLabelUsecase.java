package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.label.EditLabelRequest;

public interface UpdateLabelUsecase {
    void editLabel(Long adminId, Long labelId, EditLabelRequest request);
}
