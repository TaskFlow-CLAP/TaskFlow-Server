package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.adapter.outbound.persistense.CsvParseAdapter;
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

import java.io.IOException;
import java.util.List;

import static clap.server.application.mapper.MemberInfoMapper.toMemberInfo;
import static clap.server.application.mapper.MemberMapper.toMember;

@ApplicationService
@RequiredArgsConstructor
class RegisterMemberService implements RegisterMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;
    private final PasswordEncoder passwordEncoder;
    private final CsvParseAdapter csvParser; // CsvParseAdapter 필드 추가

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        Department department = loadDepartmentPort.findById(request.departmentId())
                .orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
        MemberInfo memberInfo = toMemberInfo(request.name(), request.email(), request.nickname(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        Member member = toMember(memberInfo);
        member.register(admin);
        commandMemberPort.save(member);
    }

    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) {
        try {
            // CSV 파일 파싱
            List<RegisterMemberRequest> memberRequests = csvParser.parse(file);

            // 기존 단일 회원 등록 로직 재사용
            memberRequests.forEach(request -> registerMember(adminId, request));

            return memberRequests.size();
        } catch (IOException e) {
            throw new RuntimeException("CSV 파일 처리 중 오류 발생", e);
        }
    }
}
