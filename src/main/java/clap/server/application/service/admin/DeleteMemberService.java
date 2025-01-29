package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUsecase {
    private final MemberService memberService;

    @Override
    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }
}

