package clap.server.application.service.admin;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.service.admin.CsvParseService;
import clap.server.application.service.admin.RegisterMemberCSVService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RegisterMemberCSVServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private CommandMemberPort commandMemberPort;

    @Mock
    private CsvParseService csvParser;

    @Mock
    private LoadMemberPort loadMemberPort;

    @InjectMocks
    private RegisterMemberCSVService registerMemberCSVService;

    // 더미 Department: department는 not null이어야 하므로, 예시로 departmentId가 채워진 객체를 생성합니다.
    private Department dummyDepartment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyDepartment = Department.builder().departmentId(100L).build();
    }

    @Test
    void testRegisterMembersFromCsv_success() throws Exception {
        // given
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv",
                "header\nrow1\nrow2".getBytes());

        // CSV 파싱 결과로 반환할 회원 객체들 (각 MemberInfo에 dummyDepartment 적용)
        Member csvMember1 = mock(Member.class);
        Member csvMember2 = mock(Member.class);

        MemberInfo dummyMemberInfo1 = MemberInfo.builder()
                .name("John Doe")
                .email("john@example.com")
                .nickname("johnny")
                .isReviewer(false)
                .department(dummyDepartment)
                .role(MemberRole.ROLE_USER)
                .departmentRole("Dept Role")
                .build();

        MemberInfo dummyMemberInfo2 = MemberInfo.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .nickname("janie")
                .isReviewer(false)
                .department(dummyDepartment)
                .role(MemberRole.ROLE_USER)
                .departmentRole("Dept Role")
                .build();

        when(csvMember1.getMemberInfo()).thenReturn(dummyMemberInfo1);
        when(csvMember2.getMemberInfo()).thenReturn(dummyMemberInfo2);
        List<Member> csvMembers = Arrays.asList(csvMember1, csvMember2);
        when(csvParser.parseDataAndMapToMember(file)).thenReturn(csvMembers);

        when(loadMemberPort.findByNickname(anyString())).thenReturn(Optional.empty());
        when(loadMemberPort.findByEmail(anyString())).thenReturn(Optional.empty());

        Member adminMember = mock(Member.class);
        when(memberService.findActiveMember(adminId)).thenReturn(adminMember);

        Member newMember1 = mock(Member.class);
        Member newMember2 = mock(Member.class);

        try (MockedStatic<Member> mockedStatic = Mockito.mockStatic(Member.class)) {
            mockedStatic.when(() -> Member.createMember(eq(adminMember), eq(dummyMemberInfo1)))
                    .thenReturn(newMember1);
            mockedStatic.when(() -> Member.createMember(eq(adminMember), eq(dummyMemberInfo2)))
                    .thenReturn(newMember2);

            // when
            int result = registerMemberCSVService.registerMembersFromCsv(adminId, file);

            // then
            ArgumentCaptor<List<Member>> captor = ArgumentCaptor.forClass(List.class);
            verify(commandMemberPort).saveAll(captor.capture());
            List<Member> savedMembers = captor.getValue();

            assertEquals(2, savedMembers.size(), "CSV 파싱된 회원 수 만큼 새로운 회원이 생성");
            assertEquals(2, result, "등록된 회원 수는 CSV 파일의 회원 수와 동일");

            mockedStatic.verify(() -> Member.createMember(adminMember, dummyMemberInfo1), times(1));
            mockedStatic.verify(() -> Member.createMember(adminMember, dummyMemberInfo2), times(1));
        }
    }

    @Test
    void testRegisterMembersFromCsv_duplicateThrowsException() throws Exception {
        // given
        Long adminId = 1L;
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv",
                "header\nrow1".getBytes());

        Member csvMember1 = mock(Member.class);
        MemberInfo dummyMemberInfo1 = MemberInfo.builder()
                .name("John Doe")
                .email("john@example.com")
                .nickname("johnny")
                .isReviewer(false)
                .department(dummyDepartment)
                .role(MemberRole.ROLE_USER)
                .departmentRole("Dept Role")
                .build();
        when(csvMember1.getMemberInfo()).thenReturn(dummyMemberInfo1);
        List<Member> csvMembers = Arrays.asList(csvMember1);
        when(csvParser.parseDataAndMapToMember(file)).thenReturn(csvMembers);

        Member adminMember = mock(Member.class);
        when(memberService.findActiveMember(adminId)).thenReturn(adminMember);

        when(loadMemberPort.existsByNicknamesOrEmails(Set.of(dummyMemberInfo1.getNickname()), Set.of(dummyMemberInfo1.getEmail())))
                .thenReturn(true);

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                registerMemberCSVService.registerMembersFromCsv(adminId, file)
        );
        assertEquals(MemberErrorCode.DUPLICATE_NICKNAME_OR_EMAIL.getMessage(), exception.getMessage(),
                "중복된 닉네임이나 email이 존재하면 MEMBER_012 예외가 발생해야 합니다.");
    }
}
