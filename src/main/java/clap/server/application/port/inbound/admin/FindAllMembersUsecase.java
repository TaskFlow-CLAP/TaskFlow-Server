package clap.server.application.port.inbound.admin;

import clap.server.domain.model.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllMembersUsecase {
    Page<Member> findAllMembers(Pageable pageable);
}