package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static clap.server.application.mapper.MemberInfoMapper.toMemberInfo;
import static clap.server.application.mapper.MemberMapper.toMember;

@ApplicationService
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;
    private final PasswordEncoder passwordEncoder;
    private final CsvParseService csvParser;

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        Department department = loadDepartmentPort.findById(request.departmentId())
                .orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        MemberInfo memberInfo = toMemberInfo(
                request.name(),
                request.email(),
                request.nickname(),
                request.isReviewer(),
                department,
                request.role(),
                request.departmentRole()
        );

        Member member = toMember(memberInfo);
        member.register(admin);

        commandMemberPort.save(member);
    }

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