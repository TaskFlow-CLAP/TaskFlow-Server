package clap.server.application.port.inbound.admin;

import org.springframework.web.multipart.MultipartFile;

public interface RegisterMemberCSVUsecase {
    int registerMembersFromCsv(Long adminId, MultipartFile file);
}
