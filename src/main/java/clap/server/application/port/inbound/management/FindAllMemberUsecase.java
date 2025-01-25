package clap.server.application.port.inbound.management;

import clap.server.domain.model.member.Member;

import java.util.List;

public interface FindAllMemberUsecase {
    List<Member> findAllMembers();
}
