package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FindAllMembersUsecase {
    PageResponse<RetrieveAllMemberResponse> findAllMembers(Pageable pageable);
}
