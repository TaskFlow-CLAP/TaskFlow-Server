package clap.server.application.service.admin;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterMemberCSVServiceTest {

    private RegisterMemberCSVService registerMemberCSVService;
    private MemberService memberService;
    private CommandMemberPort commandMemberPort;
    private CsvParseService csvParseService;


    @BeforeEach
    void setup() {
        memberService = Mockito.mock(MemberService.class);
        commandMemberPort = Mockito.mock(CommandMemberPort.class);
        csvParseService = Mockito.mock(CsvParseService.class);
        registerMemberCSVService = new RegisterMemberCSVService(memberService, commandMemberPort, csvParseService);
    }

    /**
     *  정상적인 회원 등록 테스트
     * - 주어진 CSV 파일을 정상적으로 파싱하여 회원이 등록되는지 검증
     */
    @Test
    void testRegisterMembersFromCsvSuccess() throws IOException {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        Member admin = Mockito.mock(Member.class);
        List<Member> parsedMembers = List.of(Mockito.mock(Member.class), Mockito.mock(Member.class));

        when(memberService.findActiveMember(adminId)).thenReturn(admin);
        when(csvParseService.parseDataAndMapToMember(file)).thenReturn(parsedMembers);

        int addedCount = registerMemberCSVService.registerMembersFromCsv(adminId, file);

        assertEquals(2, addedCount);
        verify(commandMemberPort, times(2)).save(any(Member.class));
        verify(parsedMembers.get(0), times(1)).register(admin);
        verify(parsedMembers.get(1), times(1)).register(admin);
    }

    /**
     * ❌ 관리자 찾기 실패 (MEMBER_NOT_FOUND)
     */
    @Test
    void testRegisterMembersFromCsvThrowsWhenAdminNotFound() {
        Long adminId = 99L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        when(memberService.findActiveMember(adminId)).thenThrow(new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        // 검증: 발생한 예외가 `MEMBER_NOT_FOUND`인지 확인
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND.getCustomCode(), exception.getCode().getCustomCode());
        verifyNoInteractions(commandMemberPort); // 회원 저장 로직이 실행안됨
    }

    /**
     * ❌ CSV 파싱 실패 (CSV_PARSING_ERROR)
     */
    @Test
    void testRegisterMembersFromCsvThrowsWhenCsvParsingFails() {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        // ✅ Mock 객체 설정: CSV 파싱 과정에서 예외 발생하도록 설정
        when(csvParseService.parseDataAndMapToMember(file)).thenThrow(new ApplicationException(MemberErrorCode.CSV_PARSING_ERROR));

        // 🔹 유스케이스 실행 및 예외 검증
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        // ✅ 검증: 발생한 예외가 `CSV_PARSING_ERROR`인지 확인
        assertEquals(MemberErrorCode.CSV_PARSING_ERROR.getCustomCode(), exception.getCode().getCustomCode());
        verifyNoInteractions(commandMemberPort); // ❗ 회원 저장 로직이 실행되지 않아야 함
    }

    /**
     * ❌ 회원 등록 실패 (MEMBER_REGISTRATION_FAILED)
     *
     */
    @Test
    void testRegisterMembersFromCsvThrowsWhenSavingMemberFails() {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        Member admin = Mockito.mock(Member.class);
        Member failingMember = Mockito.mock(Member.class);
        List<Member> parsedMembers = List.of(failingMember, Mockito.mock(Member.class));

        //  특정 회원 등록 중 예외 발생
        when(memberService.findActiveMember(adminId)).thenReturn(admin);
        when(csvParseService.parseDataAndMapToMember(file)).thenReturn(parsedMembers);
        doThrow(new ApplicationException(MemberErrorCode.MEMBER_REGISTRATION_FAILED))
                .when(commandMemberPort).save(failingMember);

        // Usecase 실행
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        assertEquals(MemberErrorCode.MEMBER_REGISTRATION_FAILED.getCustomCode(), exception.getCode().getCustomCode());
        verify(commandMemberPort, times(1)).save(failingMember); // ❗ 실패한 회원만 저장 시도해야 함
    }
}
