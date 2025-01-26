package clap.server.application.mapper;


import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.domain.model.member.Department;
import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;

public class MemberMapper {
    private MemberMapper() {
        throw new IllegalArgumentException();
    }

    public static Member toMember(MemberInfo memberInfo) {
        return Member.builder()
                .memberInfo(memberInfo)
                .build();
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