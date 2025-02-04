package clap.server.application.port.inbound.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RegisterMemberCSVUsecase {
    int registerMembersFromCsv(Long adminId, MultipartFile file) throws IOException;
}
