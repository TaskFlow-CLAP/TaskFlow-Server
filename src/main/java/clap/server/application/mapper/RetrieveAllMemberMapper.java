package clap.server.application.mapper;

import clap.server.adapter.inbound.web.member.RetrieveAllMemberResponse;
import clap.server.domain.model.member.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetrieveAllMemberMapper {
    public List<RetrieveAllMemberResponse> toResponseList(List<Member> members) {
        return members.stream()
                .map(this::toResponse)
                .toList();
    }

    public RetrieveAllMemberResponse toResponse(Member member) {
        return new RetrieveAllMemberResponse(
                member.getMemberInfo().getName(),
                member.getMemberInfo().getEmail(),
                member.getMemberInfo().getNickname(),
                member.getMemberInfo().isReviewer(),
                member.getMemberInfo().getDepartment().getDepartmentId(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartmentRole()
        );
    }
}