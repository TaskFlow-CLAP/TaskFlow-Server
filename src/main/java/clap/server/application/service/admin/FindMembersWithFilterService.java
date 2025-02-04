package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.application.port.inbound.admin.FindMembersWithFilterUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMembersWithFilterService implements FindMembersWithFilterUsecase {
    private final LoadMemberPort loadMemberPort;

    @Override
    public Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest) {
        return loadMemberPort.findMembersWithFilter(pageable, filterRequest);
    }
}
