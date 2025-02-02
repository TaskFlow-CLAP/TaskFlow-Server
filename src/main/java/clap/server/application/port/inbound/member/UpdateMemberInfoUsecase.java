package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.UpdateMemberInfoRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateMemberInfoUsecase {
    void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request, MultipartFile profileImage) throws IOException;
}
