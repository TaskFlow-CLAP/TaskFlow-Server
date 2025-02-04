package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskItemResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.task.TeamStatusController;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.service.task.TeamStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "spring.flyway.enabled=false")
@AutoConfigureMockMvc
class TeamStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamStatusController teamStatusController;

    @MockBean
    private TeamStatusService teamStatusService;

    @Container
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.5"))
            .withReuse(true);

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
    }

    @Test
    void filterTeamStatus_validRequest_shouldReturnValidResponse() {
        // 필터 객체 생성
        FilterTeamStatusRequest filter = new FilterTeamStatusRequest(
                "기여도순", // 정렬 기준
                List.of(1L, 2L), // 1차 카테고리 ID 목록
                List.of(1L, 2L), // 2차 카테고리 ID 목록
                "타이틀1" // 작업 타이틀
        );

        // 컨트롤러 메서드 호출
        ResponseEntity<TeamStatusResponse> responseEntity = teamStatusController.filterTeamStatus(filter);

        // 응답 값 검증
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().members()).isNotEmpty();
    }

    @Test
    void filterTeamStatus_invalidRequest_shouldReturnValidResponse() {
        // 잘못된 필터 객체 생성 (빈 리스트 혹은 null 값)
        FilterTeamStatusRequest filter = new FilterTeamStatusRequest(
                "", // 잘못된 정렬 기준
                null, // 잘못된 1차 카테고리 ID 목록
                null, // 잘못된 2차 카테고리 ID 목록
                "" // 잘못된 작업 타이틀
        );

        // 컨트롤러 메서드 호출
        ResponseEntity<TeamStatusResponse> responseEntity = teamStatusController.filterTeamStatus(filter);

        // 200 OK를 확인
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testFilterTeamStatus() throws Exception {
        // mock 응답 설정
        TeamStatusResponse response = new TeamStatusResponse(List.of());

        // mock 동작 설정
        when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class))).thenReturn(response);

        // mockMvc 요청 수행
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "기여도순")  // 정렬 기준
                        .param("mainCategoryIds", "1")  // 1차 카테고리 ID 목록
                        .param("categoryIds", "3")  // 2차 카테고리 ID 목록
                        .param("taskTitle", ""))  // 작업 타이틀
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").exists());
    }
    @Test
    public void testFilterTeamStatus_validRequest_shouldReturnFilteredResults() throws Exception {
        // mock 응답 설정: 예상되는 데이터
        List<TeamMemberTaskResponse> mockResponseData = List.of(
                new TeamMemberTaskResponse(
                        1L,
                        "사용자1",
                        "imageUrl1",
                        "부서1",
                        2,
                        1,
                        3,
                        List.of(new TaskItemResponse(1L, "code1", "타이틀1", "1", "2", "요청자1", "imageUrl", "부서", 1, TaskStatus.COMPLETED, LocalDateTime.now())))
        );

        // mock 동작 설정: filterTeamStatus 메소드를 호출
        when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class)))
                .thenReturn(new TeamStatusResponse(mockResponseData));

        // mockMvc 요청 수행: 필터링된 결과 확인
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "기여도순")  // 정렬 기준
                        .param("mainCategoryIds", "1")  // 1차 카테고리 ID 목록
                        .param("categoryIds", "2")  // 2차 카테고리 ID 목록
                        .param("taskTitle", "타이틀1"))  // 작업 타이틀
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").exists())  // members가 존재하는지 확인
                .andExpect(jsonPath("$.members[0].tasks[0].title").value("타이틀1"))  // 첫 번째 작업의 타이틀 확인
                .andExpect(jsonPath("$.members[0].tasks[0].taskStatus").value("IN_PROGRESS"))  // 작업 상태 확인
                .andDo(print());
    }


    @Test
    public void testFilterTeamStatus_emptyResponse_shouldReturnEmpty() throws Exception {
        // mock 응답 설정: 빈 리스트 반환 (조건에 맞는 작업이 없는 경우)
        List<TeamMemberTaskResponse> mockResponseData = List.of();

        /// mock 동작 설정: filterTeamStatus 메소드를 호출
                when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class)))
                       .thenReturn(new TeamStatusResponse(mockResponseData));

        // mockMvc 요청 수행: 필터링된 결과가 없을 경우
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "기여도순")  // 정렬 기준
                        .param("mainCategoryIds", "1")  // 1차 카테고리 ID 목록
                        .param("categoryIds", "2")  // 2차 카테고리 ID 목록
                        .param("taskTitle", "타이틀1"))  // 작업 타이틀
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isEmpty());  // members가 비어 있는지 확인
    }

    @Test
    public void testFilterTeamStatus_invalidRequest_shouldReturnBadRequest() throws Exception {
        // 잘못된 요청: 필터 값이 비어있거나 잘못됨
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "")  // 잘못된 정렬 기준
                        .param("mainCategoryIds", "")  // 잘못된 1차 카테고리 ID 목록
                        .param("categoryIds", "")  // 잘못된 2차 카테고리 ID 목록
                        .param("taskTitle", ""))  // 잘못된 작업 타이틀
                .andExpect(status().isBadRequest());  // 400 Bad Request 응답을 예상
    }
    @Test
    public void testFilterTeamStatus_completedTaskShouldNotBeReturned() throws Exception {
        // mock 응답 설정: 'COMPLETED' 상태인 작업이 제외되어야 함
        List<TeamMemberTaskResponse> mockResponseData = List.of(
                new TeamMemberTaskResponse(
                        1L,
                        "사용자1",
                        "imageUrl1",
                        "부서1",
                        2,
                        1,
                        3,
                        List.of(
                                new TaskItemResponse(1L, "code1", "타이틀1", "1", "2", "요청자1", "imageUrl", "부서", 1, TaskStatus.IN_PROGRESS, LocalDateTime.now()),
                                new TaskItemResponse(2L, "code2", "타이틀2", "1", "2", "요청자2", "imageUrl", "부서", 2, TaskStatus.COMPLETED, LocalDateTime.now())
                        )
                )
        );

        // mock 동작 설정: filterTeamStatus 메소드 호출
        when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class)))
                .thenReturn(new TeamStatusResponse(mockResponseData));

        // mockMvc 요청 수행: 필터링된 결과 확인
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "기여도순")  // 정렬 기준
                        .param("mainCategoryIds", "1")  // 1차 카테고리 ID 목록
                        .param("categoryIds", "2")  // 2차 카테고리 ID 목록
                        .param("taskTitle", "타이틀"))  // 작업 타이틀
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members[0].tasks").isArray())  // tasks가 배열 형태인지 확인
                .andExpect(jsonPath("$.members[0].tasks.length()").value(1))  // 완료된 작업은 제외되어야 하므로 tasks 배열 길이가 1이어야 함
                .andExpect(jsonPath("$.members[0].tasks[0].taskStatus").value("IN_PROGRESS"));  // 첫 번째 작업의 상태는 IN_PROGRESS이어야 함
    }




}
