package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindMembersWithFilterUsecase {
    PageResponse<RetrieveAllMemberResponse> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest);
}