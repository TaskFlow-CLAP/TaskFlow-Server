package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.adapter.outbound.persistense.CsvParseAdapter;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterMemberCsvService implements RegisterMemberUsecase {
    private final CsvParseAdapter csvParser;

    @Override
    @Transactional
    public int registerMembersFromCsv(Long adminId, MultipartFile file) {
        try {
            // CSV 파싱
            List<RegisterMemberRequest> memberRequests = csvParser.parse(file);

            // 각 회원 등록 호출
            memberRequests.forEach(request -> registerMember(adminId, request));

            return memberRequests.size();
        } catch (IOException e) {
            throw new RuntimeException("CSV 파일 처리 중 오류 발생", e);
        }
    }

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        // 기존 단일 회원 추가 로직 그대로 유지
    }
}
