package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<MemberEntity> findAllMembers(Pageable pageable);
    Page<MemberEntity> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest, String sortDirection) ;
}
