package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Member;

import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> findById(Long id);
}
