package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CsvParseAdapter {

    public List<RegisterMemberRequest> parse(MultipartFile file) {
        List<RegisterMemberRequest> memberRequests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 7) {
                    throw ApplicationException.from(MemberErrorCode.INVALID_CSV_FORMAT);
                }
                memberRequests.add(mapToRegisterMemberRequest(fields));
            }
        } catch (IOException e) {
            throw ApplicationException.from(MemberErrorCode.CSV_PARSING_ERROR);
        }
        return memberRequests;
    }

    private RegisterMemberRequest mapToRegisterMemberRequest(String[] fields) {
        return new RegisterMemberRequest(
                fields[0].trim(), // name
                fields[4].trim(), // email
                fields[1].trim(), // nickname
                Boolean.parseBoolean(fields[6].trim()), // isReviewer
                Long.parseLong(fields[2].trim()), // departmentId
                MemberRole.valueOf(fields[5].trim()), // role
                fields[3].trim() // departmentRole
        );
    }
}
