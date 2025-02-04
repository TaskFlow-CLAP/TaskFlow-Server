package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.RegisterMemberCSVUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.utils.FileTypeValidator;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.FileErrorcode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class RegisterMemberCSVService implements RegisterMemberCSVUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final CsvParseService csvParser;

    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) throws IOException {
        if (!FileTypeValidator.validCSVFile(file.getInputStream())) {
            throw new ApplicationException(FileErrorcode.UNSUPPORTED_FILE_TYPE);
        }

        List<Member> members = csvParser.parseDataAndMapToMember(file);
        Member admin = memberService.findActiveMember(adminId);
        members.forEach(member -> {member.register(admin);});

        commandMemberPort.saveAll(members);
        return members.size();
    }
}