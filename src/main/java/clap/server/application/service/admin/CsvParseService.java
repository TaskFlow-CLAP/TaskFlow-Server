package clap.server.application.service.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.domain.policy.member.ManagerDepartmentPolicy;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static clap.server.domain.model.member.MemberInfo.toMemberInfo;


@Slf4j
@Service
@RequiredArgsConstructor
public class CsvParseService {

    private final LoadDepartmentPort loadDepartmentPort;
    private final ManagerDepartmentPolicy managerDepartmentPolicy;

    public List<Member> parseDataAndMapToMember(MultipartFile file) {
        List<Member> members = new ArrayList<>();
        List<Department> departments = loadDepartmentPort.findActiveDepartments();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new ApplicationException(MemberErrorCode.INVALID_CSV_FORMAT);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 7) {
                    throw new ApplicationException(MemberErrorCode.INVALID_CSV_FORMAT);
                }
                members.add(mapToMember(fields, departments));
            }
        } catch (IOException e) {
            throw new ApplicationException(MemberErrorCode.CSV_PARSING_ERROR);
        }
        return members;
    }

    private Member mapToMember(String[] fields, List<Department> departments) {
        Long departmentId = Long.parseLong(fields[2].trim());
        Department department = departments.stream()
                .filter(dept -> dept.getDepartmentId().equals(departmentId))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        managerDepartmentPolicy.validateDepartment(department, MemberRole.valueOf(fields[5].trim()));
        MemberInfo memberInfo = toMemberInfo(
                fields[0].trim(), // name
                fields[4].trim(), // email
                fields[1].trim(), // nickname
                Boolean.parseBoolean(fields[6].trim().toLowerCase()), // isReviewer
                department, // department
                MemberRole.valueOf(fields[5].trim()), // role
                fields[3].trim() // departmentRole
        );

        return Member.builder()
                .memberInfo(memberInfo)
                .build();
    }
}
