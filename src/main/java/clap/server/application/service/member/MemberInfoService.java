package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.MemberInfoUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.MemberMapper.toMemberProfileResponse;

@ApplicationService
@RequiredArgsConstructor
class MemberInfoService implements MemberInfoUsecase {
    private final MemberService memberService;
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;


    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        return toMemberProfileResponse(member);
    }
}
