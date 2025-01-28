package clap.server.application.port.inbound.management;

import clap.server.domain.model.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FindAllMemberUsecase {
    Page<Member> findAllMembers(Pageable pageable);
}
