package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.MemberDetailInfoUsecase;
import clap.server.application.port.inbound.member.MemberProfileUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.MemberMapper.toMemberDetailInfoResponse;
import static clap.server.application.mapper.MemberMapper.toMemberProfileResponse;

@ApplicationService
@RequiredArgsConstructor
class MemberInfoService implements MemberProfileUsecase , MemberDetailInfoUsecase {
    private final MemberService memberService;

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        return toMemberProfileResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailInfoResponse getMemberInfo(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        return toMemberDetailInfoResponse(member);
    }
}
