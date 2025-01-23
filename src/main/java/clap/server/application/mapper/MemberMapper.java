package clap.server.application.mapper;

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
}
