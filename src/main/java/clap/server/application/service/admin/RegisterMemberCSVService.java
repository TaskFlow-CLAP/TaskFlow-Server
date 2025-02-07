package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.RegisterMemberCSVUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
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
    private final LoadMemberPort loadMemberPort;


    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) {
        List<Member> members = csvParser.parseDataAndMapToMember(file);
        Member admin = memberService.findActiveMember(adminId);

        members.forEach(member -> {
            String nickname = member.getMemberInfo().getNickname();
            String email = member.getMemberInfo().getEmail();
            if (loadMemberPort.findByNickname(nickname).isPresent() ||
                    loadMemberPort.findByEmail(email).isPresent()) {
                throw new ApplicationException(MemberErrorCode.DUPLICATE_NICKNAME_OR_EMAIL);
            }
        });

        List<Member> newMembers = members.stream()
                .map(memberData -> Member.createMember(admin, memberData.getMemberInfo()))
                .toList();

        commandMemberPort.saveAll(newMembers);
        return members.size();
    }
}