package clap.server.application.port.inbound.management;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import org.springframework.web.multipart.MultipartFile;

public interface RegisterMemberCsvUsecase {
    void registerMember(Long adminId, RegisterMemberRequest request);

    int registerMembersFromCsv(Long adminId, MultipartFile file);
}
