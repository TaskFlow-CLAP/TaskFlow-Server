package clap.server.application.service.admin;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
     * ✅ 정상적인 회원 등록 테스트
     */
    @Test
    @DisplayName("CSV 파일에서 회원 정보를 성공적으로 파싱하고 등록하는지 검증한다.")
    void testRegisterMembersFromCsvSuccess() {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "name,nickname,departmentId,departmentRole,email,role,isReviewer\nJohn,JohnDoe,1,Manager,john.doe@example.com,ROLE_USER,TRUE".getBytes());

        Member admin = Mockito.mock(Member.class);
        Member member1 = Mockito.mock(Member.class);
        Member member2 = Mockito.mock(Member.class);
        List<Member> parsedMembers = List.of(member1, member2);

        when(memberService.findActiveMember(adminId)).thenReturn(admin);
        when(csvParseService.parseDataAndMapToMember(file)).thenReturn(parsedMembers);

        int addedCount = registerMemberCSVService.registerMembersFromCsv(adminId, file);

        assertEquals(2, addedCount); // 2명이 성공적으로 등록됨
        verify(commandMemberPort).saveAll(parsedMembers); // 모든 회원 저장
        verify(member1).register(admin); // 관리자와 연결
        verify(member2).register(admin); // 관리자와 연결
    }

    /**
     * ❌ 관리자 찾기 실패 (MEMBER_NOT_FOUND)
     */
    @Test
    @DisplayName("관리자가 존재하지 않을 때 CSV 회원 등록 시 예외가 발생한다.")
    void testRegisterMembersFromCsvThrowsWhenAdminNotFound() {
        Long adminId = 99L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        when(memberService.findActiveMember(adminId)).thenThrow(new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND.getCustomCode(), exception.getCode().getCustomCode());
        verifyNoInteractions(commandMemberPort); // 저장 로직 실행 안됨
    }

    /**
     * ❌ CSV 파싱 실패 (CSV_PARSING_ERROR)
     */
    @Test
    @DisplayName("CSV 파싱 실패 시 예외 발생 및 회원 등록이 실패한다.")
    void testRegisterMembersFromCsvThrowsWhenCsvParsingFails() {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        when(csvParseService.parseDataAndMapToMember(file)).thenThrow(new ApplicationException(MemberErrorCode.CSV_PARSING_ERROR));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        assertEquals(MemberErrorCode.CSV_PARSING_ERROR.getCustomCode(), exception.getCode().getCustomCode());
        verifyNoInteractions(commandMemberPort); // 저장 로직 실행 안됨
    }

    /**
     * ❌ 회원 등록 실패 (MEMBER_REGISTRATION_FAILED)
     */
    @Test
    @DisplayName("회원 등록 과정 중 실패 시 예외 발생 및 회원 저장이 중단된다.")
    void testRegisterMembersFromCsvThrowsWhenSavingMemberFails() {
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", "dummy-content".getBytes());

        Member admin = Mockito.mock(Member.class);
        Member failingMember = Mockito.mock(Member.class);
        List<Member> parsedMembers = List.of(failingMember);

        when(memberService.findActiveMember(adminId)).thenReturn(admin);
        when(csvParseService.parseDataAndMapToMember(file)).thenReturn(parsedMembers);
        doThrow(new ApplicationException(MemberErrorCode.MEMBER_REGISTRATION_FAILED))
                .when(commandMemberPort).saveAll(parsedMembers);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            registerMemberCSVService.registerMembersFromCsv(adminId, file);
        });

        assertEquals(MemberErrorCode.MEMBER_REGISTRATION_FAILED.getCustomCode(), exception.getCode().getCustomCode());
        verify(commandMemberPort).saveAll(parsedMembers);
    }
}
