package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Member;

import java.util.Optional;

public interface CommandMemberPort {
    void save(Member member);
}
