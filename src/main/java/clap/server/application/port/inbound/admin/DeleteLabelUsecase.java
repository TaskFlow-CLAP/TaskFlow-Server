package clap.server.application.port.inbound.admin;

public interface DeleteLabelUsecase {
    void deleteLabel(Long adminId, Long labelId);
}
