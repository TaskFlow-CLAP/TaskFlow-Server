package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.adapter.outbound.persistense.CsvParseAdapter;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterMemberCsvService implements RegisterMemberUsecase {
    private final CsvParseAdapter csvParser;

    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) {
        try {
            List<RegisterMemberRequest> memberRequests = csvParser.parse(file);

            int successCount = 0;
            for (RegisterMemberRequest request : memberRequests) {
                try {
                    registerMember(adminId, request);
                    successCount++;
                } catch (Exception e) {
                    throw ApplicationException.from(MemberErrorCode.MEMBER_REGISTRATION_FAILED);
                }
            }
            return successCount;
        } catch (IOException e) {
            throw ApplicationException.from(MemberErrorCode.CSV_PARSING_ERROR);
        }
    }

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
    }
}
