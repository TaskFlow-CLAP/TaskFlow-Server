package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.domain.model.member.Member;

public class MemberMapper {
    private MemberMapper() {
        throw new IllegalArgumentException();
    }

    public static MemberProfileResponse toMemberProfileResponse(Member member) {
        return new MemberProfileResponse(
                member.getMemberId(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getImageUrl(),
                member.getMemberInfo().getRole()
        );
    }
}