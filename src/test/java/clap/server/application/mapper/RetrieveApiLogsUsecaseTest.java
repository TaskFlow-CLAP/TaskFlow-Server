package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.application.service.log.FindApiLogsService;
import clap.server.application.port.inbound.domain.LoginDomainService;
import clap.server.domain.model.log.ApiLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RetrieveApiLogsUsecaseTest {

    private final ApiLogRepositoryPort apiLogRepositoryPort = mock(ApiLogRepositoryPort.class);
    private final LoginDomainService loginDomainService = mock(LoginDomainService.class);
    private final FindApiLogsService usecase = new FindApiLogsService(apiLogRepositoryPort, loginDomainService);

    @Test
    void getMemberLogs_ShouldReturnConvertedResponses() {
        // MemberEntity 생성
        MemberEntity mockMemberEntity = MemberEntity.builder()
                .memberId(5L) // 정확한 memberId 설정
                .build();

        // MemberLogEntity 생성
        MemberLogEntity mockEntity = MemberLogEntity.builder()
                .logId(2L)
                .member(mockMemberEntity) // member 필드에 mockMemberEntity 설정
                .requestAt(LocalDateTime.now())
                .responseAt(LocalDateTime.now())
                .requestUrl("/api/data")
                .requestMethod(ApiHttpMethod.GET)
                .statusCode(200)
                .customStatusCode("SUCCESS")
                .build();

        // 디버깅: mockEntity 생성 확인
        System.out.println("DEBUG: Created mockEntity - Member ID: " + mockEntity.getMember().getMemberId());

        // Mock 리포지토리 설정
        when(apiLogRepositoryPort.findMemberLogs()).thenReturn(List.of(mockEntity));

        // 테스트 실행
        List<MemberLogResponse> responses = usecase.getMemberLogs();

        // 디버깅: 반환된 responses 확인
        // 결과 검증
        assertThat(responses).hasSize(1); // 리스트 크기 확인
        MemberLogResponse response = responses.get(0);
        assertThat(response.logId()).isEqualTo(mockEntity.getLogId()); // logId 검증
        assertThat(response.memberId()).isEqualTo(5L); // memberId 검증
        verify(apiLogRepositoryPort, times(1)).findMemberLogs(); // 리포지토리 호출 검증
    }

    @Test
    void getAnonymousLogs_ShouldReturnConvertedResponses() {
        // AnonymousLogEntity 생성
        AnonymousLogEntity mockEntity = AnonymousLogEntity.builder()
                .logId(1L)
                .loginNickname("testUser")
                .requestAt(LocalDateTime.now())
                .responseAt(LocalDateTime.now())
                .requestUrl("/api/login")
                .requestMethod(ApiHttpMethod.POST)
                .statusCode(200)
                .customStatusCode("OK")
                .build();

        // 디버깅: mockEntity 생성 확인
        System.out.println("DEBUG: Created mockEntity - Login Nickname: " + mockEntity.getLoginNickname());

        // Mock 서비스 설정
        when(apiLogRepositoryPort.findAnonymousLogs("로그인 시도")).thenReturn(List.of(mockEntity));
        when(loginDomainService.getFailedAttemptCount("testUser")).thenReturn(3); // 실패 시도 수 설정

        // 테스트 실행
        List<AnonymousLogResponse> responses = usecase.getAnonymousLogs();

        // 디버깅: 반환된 responses 확인
        System.out.println("DEBUG: Received responses: " + responses);

        // 결과 검증
        assertThat(responses).hasSize(1); // 리스트 크기 확인
        AnonymousLogResponse response = responses.get(0);
        assertThat(response.logId()).isEqualTo(mockEntity.getLogId()); // logId 검증
        assertThat(response.failedAttempts()).isEqualTo(3); // 실패 시도 검증
        verify(apiLogRepositoryPort, times(1)).findAnonymousLogs("로그인 시도");
        verify(loginDomainService, times(1)).getFailedAttemptCount("testUser");
    }

    @Test
    void getAllLogs_ShouldReturnAllLogs() {
        // AnonymousLogEntity 생성
        AnonymousLogEntity anonymousLogEntity = AnonymousLogEntity.builder()
                .logId(1L)
                .loginNickname("testUser")
                .requestAt(LocalDateTime.now())
                .responseAt(LocalDateTime.now())
                .requestUrl("/api/login")
                .requestMethod(ApiHttpMethod.POST)
                .statusCode(200)
                .customStatusCode("OK")
                .build();

        // MemberLogEntity 생성
        MemberEntity mockMemberEntity = MemberEntity.builder()
                .memberId(5L)
                .build();

        MemberLogEntity memberLogEntity = MemberLogEntity.builder()
                .logId(2L)
                .member(mockMemberEntity)
                .requestAt(LocalDateTime.now())
                .responseAt(LocalDateTime.now())
                .requestUrl("/api/data")
                .requestMethod(ApiHttpMethod.GET)
                .statusCode(200)
                .customStatusCode("SUCCESS")
                .build();

        // 디버깅: mockEntities 생성 확인
        System.out.println("DEBUG: Created anonymousLogEntity - Login Nickname: " + anonymousLogEntity.getLoginNickname());
        System.out.println("DEBUG: Created memberLogEntity - Member ID: " + memberLogEntity.getMember().getMemberId());

        // Mock 리포지토리 설정
        when(apiLogRepositoryPort.findAllLogs()).thenReturn(List.of(anonymousLogEntity.toDomain(), memberLogEntity.toDomain()));

        // 테스트 실행
        List<ApiLog> logs = usecase.getApiLogs();

        // 디버깅: 반환된 logs 확인
        System.out.println("DEBUG: Received logs: " + logs);

        // 결과 검증
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getLogId()).isEqualTo(1L); // AnonymousLogEntity 검증
        assertThat(logs.get(1).getLogId()).isEqualTo(2L); // MemberLogEntity 검증
        verify(apiLogRepositoryPort, times(1)).findAllLogs();
    }
}
