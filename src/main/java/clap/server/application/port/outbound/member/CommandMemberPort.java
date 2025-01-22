package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Member;

public interface CommandMemberPort {
    void save(Member member);
}
