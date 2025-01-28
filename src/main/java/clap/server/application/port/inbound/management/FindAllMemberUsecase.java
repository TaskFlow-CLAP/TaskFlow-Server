package clap.server.application.port.inbound.management;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.domain.model.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FindAllMemberUsecase {
    Page<Member> findAllMembers(Pageable pageable);
    Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest); // 조건부 필터링 조회

}
