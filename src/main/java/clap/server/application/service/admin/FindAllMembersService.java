package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.FindAllMembersUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllMembersService implements FindAllMembersUsecase {
    private final LoadMemberPort loadMemberPort;

    @Override
    public Page<Member> findAllMembers(Pageable pageable) {
        return loadMemberPort.findAllMembers(pageable);
    }
}
