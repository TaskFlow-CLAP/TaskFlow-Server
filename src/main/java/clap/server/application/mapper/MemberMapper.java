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


    public static MemberInfo toMemberInfo(String name, String email, String nickname, boolean isReviewer,
                                          Department department, MemberRole role, String departmentRole) {
        return MemberInfo.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .isReviewer(isReviewer)
                .department(department)
                .role(role)
                .departmentRole(departmentRole)
                .build();

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