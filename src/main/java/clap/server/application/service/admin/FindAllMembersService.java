package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.application.mapper.RetrieveAllMemberMapper;
import clap.server.application.port.inbound.admin.FindAllMembersUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class FindAllMembersService implements FindAllMembersUsecase {
    private final LoadMemberPort loadMemberPort;
    private final RetrieveAllMemberMapper retrieveAllMemberMapper;


    @Transactional(readOnly = true)
    @Override
    public PageResponse<RetrieveAllMemberResponse> findAllMembers(Pageable pageable) {
        Page<Member> members = loadMemberPort.findAllMembers(pageable);
        return PageResponse.from(members.map(retrieveAllMemberMapper::toResponse));
    }
}
