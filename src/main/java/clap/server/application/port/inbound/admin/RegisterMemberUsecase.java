package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.request.RegisterMemberRequest;
import org.springframework.web.multipart.MultipartFile;

public interface RegisterMemberUsecase {
    void registerMember(Long adminId, RegisterMemberRequest request);

    int registerMembersFromCsv(Long adminId, MultipartFile file);
}
