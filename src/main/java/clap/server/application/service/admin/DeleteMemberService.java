package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;

    @Transactional
    @Override
    public void deleteMember(Long memberId) {
        Member member = loadMemberPort.findById(memberId)
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        Hibernate.initialize(member.getDepartment());

        member.setStatusDeleted();

        commandMemberPort.save(member);
    }
}
