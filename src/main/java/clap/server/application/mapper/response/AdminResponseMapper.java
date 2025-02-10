package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.domain.model.member.Member;

import java.util.List;


public class AdminResponseMapper {
    private AdminResponseMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<RetrieveAllMemberResponse> toResponseList(List<Member> members) {
        return members.stream()
                .map(AdminResponseMapper::toRetrieveAllMemberResponse)
                .toList();
    }

    public static RetrieveAllMemberResponse toRetrieveAllMemberResponse(Member member) {
        return new RetrieveAllMemberResponse(
                member.getMemberId(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getEmail(),
                member.getMemberInfo().getNickname(),
                member.getMemberInfo().isReviewer(),
                member.getMemberInfo().getDepartment().getName(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartmentRole(),
                member.getCreatedAt(),
                member.getStatus().name()
        );
    }
}
