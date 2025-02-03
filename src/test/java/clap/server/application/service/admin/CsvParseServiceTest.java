package clap.server.application.service.admin;

import clap.server.application.service.admin.CsvParseService;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CsvParseServiceTest {

    private LoadDepartmentPort loadDepartmentPort;
    private CsvParseService csvParseService;

    @BeforeEach
    public void setup() {
        loadDepartmentPort = Mockito.mock(LoadDepartmentPort.class);
        csvParseService = new CsvParseService(loadDepartmentPort);
    }

    /**
     * 정상 케이스:
     * CSV 파일에 헤더와 하나의 데이터 행이 있는 경우 올바르게 파싱되는지 검증.
     *   name, nickname, departmentId, departmentRole, email, role, isReviewer -> 실제 CSV 파일의 columm 순서
     */
    @Test
    public void testParseValidCsv() {
        // CSV 파일 (첫 번째 줄은 헤더)
        String csvContent = "name,nickname,departmentId,departmentRole,email,role,isReviewer\n" +
                "양시훈,leo,1,dev,sihun123@gmail.com,ROLE_USER,false\n";
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", csvContent.getBytes());

        Department dummyDepartment = Department.builder().departmentId(1L).name("dev").build();
        when(loadDepartmentPort.findById(1L)).thenReturn(Optional.of(dummyDepartment));

        // CSV 파싱
        java.util.List<Member> members = csvParseService.parse(file);

        // 검증
        assertNotNull(members);
        assertEquals(1, members.size());
        Member member = members.get(0);
        assertEquals("양시훈", member.getMemberInfo().getName());
        assertEquals("leo", member.getMemberInfo().getNickname());
        assertEquals("sihun123@gmail.com", member.getMemberInfo().getEmail());
        assertEquals("dev", member.getMemberInfo().getDepartment().getName());
        assertEquals("ROLE_USER", member.getMemberInfo().getRole().name());
        assertFalse(member.getMemberInfo().isReviewer());
    }

    /**
     * 잘못된 컬럼 개수 케이스:
     * CSV 파일에 데이터 행의 컬럼 수가 7개가 아니면 예외가 발생하는지 검증
     */
    @Test
    public void testParseCsvWithInvalidColumnCount() {
        // 6개 컬럼만 있는 데이터
        String csvContent = "name,nickname,departmentId,departmentRole,email,role,isReviewer\n" +
                "김민수,minsoo,2,Infra,minsoo@naver.com,ROLE_ADMIN\n";
        MultipartFile file = new MockMultipartFile("file", "members.csv", "text/csv", csvContent.getBytes());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            csvParseService.parse(file);
        });
        assertEquals(MemberErrorCode.INVALID_CSV_FORMAT.getCustomCode(), exception.getCode().getCustomCode());
    }

    /**
     * 빈 파일 케이스:
     * CSV 파일이 완전히 비어 있으면 헤더를 읽지 못해 예외가 발생하는지 검증
     */
    @Test
    public void testParseEmptyFileThrowsException() {
        String csvContent = "";
        MultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", csvContent.getBytes());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            csvParseService.parse(file);
        });
        assertEquals(MemberErrorCode.INVALID_CSV_FORMAT.getCustomCode(), exception.getCode().getCustomCode());
    }

    /**
     * IOException 발생 케이스:
     * MultipartFile의 getInputStream() 호출 시 IOException이 발생하면 예외가 발생하는지 검증
     */
    @Test
    public void testParseIOExceptionThrowsException() throws IOException {
        // getInputStream() 호출 시 IOException을 던지는 mock MultipartFile 생성
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("IO error"));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            csvParseService.parse(file);
        });
        assertEquals(MemberErrorCode.CSV_PARSING_ERROR.getCustomCode(), exception.getCode().getCustomCode());
    }
}
