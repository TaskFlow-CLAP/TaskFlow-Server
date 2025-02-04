package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.domain.model.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindMembersWithFilterUsecase {
    Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest);
}
