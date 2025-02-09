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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        validateMembers(members);

        List<Member> newMembers = members.stream()
                .map(memberData ->
                        Member.createMember(admin, memberData.getMemberInfo()))
                .toList();

        commandMemberPort.saveAll(newMembers);
        return members.size();
    }

    public void validateMembers(List<Member> members) {
        Set<String> nicknames = new HashSet<>();
        Set<String> emails = new HashSet<>();

        for (Member member : members) {
            nicknames.add(member.getMemberInfo().getNickname());
            emails.add(member.getMemberInfo().getEmail());
        }

        if(loadMemberPort.existsByNicknamesOrEmails(nicknames, emails)) {
            throw new ApplicationException(MemberErrorCode.DUPLICATE_NICKNAME_OR_EMAIL);
        }
    }

}