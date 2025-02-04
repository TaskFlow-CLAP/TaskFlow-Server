package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Member;

import java.util.List;

public interface CommandMemberPort {
    void save(Member member);
    void saveAll(List<Member> members);
}
