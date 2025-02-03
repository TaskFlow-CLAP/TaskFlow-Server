package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.RegisterMemberCSVUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class RegisterMemberCSVService implements RegisterMemberCSVUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final CsvParseService csvParser;

    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) {
        List<Member> members = csvParser.parse(file);
        Member admin = memberService.findActiveMember(adminId);
        members.forEach(member -> {
            member.register(admin);
            commandMemberPort.save(member);
        });
        return members.size();
    }
}