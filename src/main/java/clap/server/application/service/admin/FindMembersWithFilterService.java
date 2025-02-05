package clap.server.application.service.admin;


import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.application.mapper.RetrieveAllMemberMapper;
import clap.server.application.port.inbound.admin.FindMembersWithFilterUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class FindMembersWithFilterService implements FindMembersWithFilterUsecase {
    private final LoadMemberPort loadMemberPort;
    private final RetrieveAllMemberMapper retrieveAllMemberMapper;


    @Transactional(readOnly = true)
    @Override
    public PageResponse<RetrieveAllMemberResponse> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest, String sortDirection) {
        Page<Member> members = loadMemberPort.findMembersWithFilter(pageable, filterRequest,sortDirection);
        return PageResponse.from(members.map(retrieveAllMemberMapper::toResponse));
    }
}
