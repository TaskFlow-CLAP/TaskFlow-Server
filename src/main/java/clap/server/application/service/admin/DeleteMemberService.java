package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUsecase {
    private final LoadMemberPort loadMemberPort; // 조회 작업용 Port
    private final CommandMemberPort commandMemberPort; // 데이터 변경 작업용 Port

    @Override
    public void deleteMember(Long memberId) {
        Member member = loadMemberPort.findById(memberId)
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setStatusDeleted();

        commandMemberPort.save(member);
    }
}
