package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.response.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberProfileResponse;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.MemberDetailInfoUsecase;
import clap.server.application.port.inbound.member.MemberProfileUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.response.MemberResponseMapper.toMemberDetailInfoResponse;
import static clap.server.application.mapper.response.MemberResponseMapper.toMemberProfileResponse;

@ApplicationService
@RequiredArgsConstructor
class MemberInfoService implements MemberProfileUsecase , MemberDetailInfoUsecase {
    private final MemberService memberService;

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Long memberId) {
        Member member = memberService.findActiveMemberWithDepartment(memberId);
        return toMemberProfileResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailInfoResponse getMemberInfo(Long memberId) {
        Member member = memberService.findActiveMemberWithDepartment(memberId);
        return toMemberDetailInfoResponse(member);
    }
}
