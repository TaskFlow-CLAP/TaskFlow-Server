package clap.server.application.service.admin;

import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static clap.server.application.mapper.MemberResponseMapper.toMember;
import static clap.server.domain.model.member.MemberInfo.toMemberInfo;


@Slf4j
@ApplicationService
@RequiredArgsConstructor
public class CsvParseService {

    private final LoadDepartmentPort loadDepartmentPort;

    public List<Member> parse(MultipartFile file) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 7) {
                    throw ApplicationException.from(MemberErrorCode.INVALID_CSV_FORMAT);
                }
                members.add(mapToMember(fields));
            }
        } catch (IOException e) {
            throw ApplicationException.from(MemberErrorCode.CSV_PARSING_ERROR);
        }
        return members;
    }

    private Member mapToMember(String[] fields) {
        // 부서 ID로 Department 객체 조회
        Long departmentId = Long.parseLong(fields[2].trim());
        Department department = loadDepartmentPort.findById(departmentId)
                .orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        MemberInfo memberInfo = toMemberInfo(
                fields[0].trim(), // name
                fields[4].trim(), // email
                fields[1].trim(), // nickname
                Boolean.parseBoolean(fields[6].trim()), // isReviewer
                department, // department
                MemberRole.valueOf(fields[5].trim()), // role
                fields[3].trim() // departmentRole
        );

        return toMember(memberInfo);
    }
}
