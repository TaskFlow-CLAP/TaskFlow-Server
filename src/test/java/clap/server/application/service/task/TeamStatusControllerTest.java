package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskItemResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.adapter.inbound.web.task.TeamStatusController;
import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.application.mapper.TaskResponseMapper;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.domain.model.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "spring.flyway.enabled=false")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TeamStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamStatusController teamStatusController;

    @MockBean
    private TeamStatusService teamStatusService;

    @Mock
    private LoadTaskPort loadTaskPort;

    @Autowired
    private TaskPersistenceMapper taskPersistenceMapper;


    @Container
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.5"))
            .withReuse(true);

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
    }

    @Test
    void filterTeamStatus_validRequest_shouldReturnValidResponse() {
        // Mock 응답 설정
        TeamStatusResponse mockResponse = new TeamStatusResponse(List.of(
                new TeamTaskResponse(
                        1L, "사용자1", "imageUrl1", "부서1", 2, 1, 3,
                        List.of(new TeamTaskItemResponse(
                                1L, "taskCode1", "타이틀1", "카테고리1", "카테고리2",
                                new TeamTaskItemResponse.LabelInfo("라벨1", LabelColor.RED),
                                "요청자1", "imageUrl1", "부서1", 1, TaskStatus.IN_PROGRESS,
                                LocalDateTime.now()
                        ))
                )
        ));

        // MockBean 동작 설정
        when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class)))
                .thenReturn(mockResponse);

        // 필터 객체 생성
        FilterTeamStatusRequest filter = new FilterTeamStatusRequest(
                "기여도순", List.of(1L, 2L), List.of(1L, 2L), "타이틀1"
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
    public void testFilterTeamStatus_validRequest_shouldReturnFilteredResults() throws Exception {
        // mock 응답 설정: 예상되는 데이터
        List<TeamTaskResponse> mockResponseData = List.of(
                new TeamTaskResponse(
                        1L,
                        "사용자1",
                        "imageUrl1",
                        "부서1",
                        2,
                        1,
                        3,
                        List.of(
                                new TeamTaskItemResponse(
                                        1L,
                                        "taskCode1",
                                        "타이틀1",
                                        "카테고리1",
                                        "카테고리2",
                                        new TeamTaskItemResponse.LabelInfo("라벨1", LabelColor.RED),
                                        "요청자1",
                                        "imageUrl1",
                                        "부서1",
                                        1,
                                        TaskStatus.IN_PROGRESS,
                                        LocalDateTime.now())
                        )
                )
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
                .andExpect(jsonPath("$.members[0].tasks[0].requesterNickname").value("요청자1"))  // 요청자 닉네임 확인
                .andExpect(jsonPath("$.members[0].tasks[0].requesterImageUrl").value("imageUrl1"))  // 요청자 이미지 URL 확인
                .andExpect(jsonPath("$.members[0].tasks[0].requesterDepartment").value("부서1"))  // 요청자 부서 확인
                .andDo(print());
    }



    @Test
    public void testFilterTeamStatus_emptyResponse_shouldReturnEmpty() throws Exception {
        // mock 응답 설정: 빈 리스트 반환 (조건에 맞는 작업이 없는 경우)
        List<TeamTaskResponse> mockResponseData = List.of();

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
    public void testFilterTeamStatus_noFilters_shouldReturnDefaultResults() throws Exception {
        // mock 응답 설정: 기본 정렬된 데이터
        List<TeamTaskResponse> mockResponseData = List.of(
                new TeamTaskResponse(
                        2L, "사용자A", "imageUrlA", "부서A", 1, 0, 1,
                        List.of(new TeamTaskItemResponse(
                                2L, "taskCode2", "타이틀2", "카테고리1", "카테고리2",
                                new TeamTaskItemResponse.LabelInfo("라벨2", LabelColor.BLUE),
                                "요청자A", "imageUrlA", "부서A", 2, TaskStatus.IN_REVIEWING,
                                LocalDateTime.now()
                        ))
                ),
                new TeamTaskResponse(
                        1L, "사용자B", "imageUrlB", "부서B", 2, 1, 3,
                        List.of(new TeamTaskItemResponse(
                                1L, "taskCode1", "타이틀1", "카테고리1", "카테고리2",
                                new TeamTaskItemResponse.LabelInfo("라벨1", LabelColor.RED),
                                "요청자B", "imageUrlB", "부서B", 1, TaskStatus.IN_PROGRESS,
                                LocalDateTime.now()
                        ))
                )
        );

        // mock 동작 설정: filterTeamStatus 메소드를 호출하면 기본 정렬된 데이터를 반환
        when(teamStatusService.filterTeamStatus(any(FilterTeamStatusRequest.class)))
                .thenReturn(new TeamStatusResponse(mockResponseData));

        // mockMvc 요청 수행: 필터 없이 요청
        mockMvc.perform(get("/api/team-status/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").exists())  // members 리스트가 존재하는지 확인
                .andExpect(jsonPath("$.members.length()").value(2))  // 예상 데이터 개수 확인
                .andExpect(jsonPath("$.members[0].nickname").value("사용자A"))  // 기본 정렬(이름 오름차순) 확인
                .andExpect(jsonPath("$.members[1].nickname").value("사용자B"))
                .andDo(print());
    }
    @Test
    public void testEmptyValues() {
        // Given: empty values for parameters
        FilterTeamStatusRequest request = new FilterTeamStatusRequest(
                "",   // sortBy
                List.of(),   // mainCategoryIds
                List.of(),   // categoryIds
                ""    // taskTitle
        );

        // Then: the default values should be applied for null-like values
        Assertions.assertEquals("기본", request.sortBy());
        Assertions.assertEquals(List.of(), request.mainCategoryIds());
        Assertions.assertEquals(List.of(), request.categoryIds());
        Assertions.assertEquals("", request.taskTitle());
    }
    @Test
    void testFilterTeamStatusRequest_DefaultValues() {
        // 모든 값이 null로 들어오는 경우
        FilterTeamStatusRequest request = new FilterTeamStatusRequest(null, null, null, null);

        Assertions.assertEquals("기본", request.sortBy()); // 기본값 적용 확인
        Assertions.assertEquals(List.of(), request.mainCategoryIds()); // 빈 리스트로 변환 확인
        Assertions.assertEquals(List.of(), request.categoryIds()); // 빈 리스트로 변환 확인
        Assertions.assertEquals("", request.taskTitle()); // 빈 문자열 변환 확인
    }

    @Test
    void testFilterTeamStatusRequest_WithValues() {
        // 특정 값이 들어온 경우
        FilterTeamStatusRequest request = new FilterTeamStatusRequest("기여도순", List.of(1L, 2L), List.of(3L), "테스트");

        assertEquals("기여도순", request.sortBy());
        assertEquals(List.of(1L, 2L), request.mainCategoryIds());
        assertEquals(List.of(3L), request.categoryIds());
        assertEquals("테스트", request.taskTitle());
    }
    @Test
    void testFilterTeamStatus_DefaultValues() throws Exception {
        mockMvc.perform(get("/api/team-status/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray()) // 빈 배열인지 확인
                .andExpect(jsonPath("$.totalInProgressTaskCount").value(0))
                .andExpect(jsonPath("$.totalPendingTaskCount").value(0))
                .andExpect(jsonPath("$.totalTaskCount").value(0));
    }

    @Test
    void testFilterTeamStatus_WithValues() throws Exception {
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", "기여도순")
                        .param("mainCategoryIds", "10,20")
                        .param("categoryIds", "1,2")
                        .param("taskTitle", "테스트"))
                .andExpect(status().isOk());
    }



    @Test
    void givenTaskEntity_whenMappedToDomain_thenShouldConvertCorrectly() {
        // Given
        TaskEntity taskEntity = createSampleTaskEntity();

        // When
        Task task = taskPersistenceMapper.toDomain(taskEntity);

        // Then
        assertNotNull(task);
        assertEquals(taskEntity.getTaskId(), task.getTaskId());
        assertEquals(taskEntity.getTitle(), task.getTitle());
        assertEquals(taskEntity.getTaskStatus(), task.getTaskStatus());
        assertEquals(taskEntity.getProcessor().getNickname(), task.getProcessor().getNickname());
    }

    @Test
    void givenTaskEntities_whenMappedToTeamTaskResponse_thenShouldConvertCorrectly() {
        // Given
        List<TaskEntity> taskEntities = List.of(createSampleTaskEntity(), createSampleTaskEntity());

        // When
        TeamStatusResponse response = TaskResponseMapper.toTeamStatusResponse(taskEntities);

        // Then
        assertNotNull(response);
        assertFalse(response.members().isEmpty());
        assertEquals(2, response.members().get(0).totalTaskCount());
    }

    private TaskEntity createSampleTaskEntity() {
        MemberEntity processor = MemberEntity.builder()
                .memberId(1L)
                .nickname("닉네임")
                .imageUrl("이미지 URL")
                .department(DepartmentEntity.builder().name("개발팀").build()) // Builder 사용
                .build();

        MemberEntity requester = MemberEntity.builder()
                .memberId(2L)
                .nickname("요청자 닉네임")
                .imageUrl("요청자 이미지 URL")
                .department(DepartmentEntity.builder().name("운영팀").build()) // Builder 사용
                .build();

        CategoryEntity category = CategoryEntity.builder()
                .categoryId(10L)
                .name("기본 카테고리")
                .mainCategory(CategoryEntity.builder().categoryId(1L).name("메인 카테고리").build())
                .build();

        LabelEntity label = LabelEntity.builder()
                .labelName("긴급")
                .labelColor(LabelColor.RED)
                .build();

        return TaskEntity.builder()
                .taskId(100L)
                .taskCode("TASK-001")
                .title("테스트 작업")
                .category(category)
                .label(label)
                .requester(requester)
                .processor(processor)
                .processorOrder(1L)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
    }
    @Test
    public void testFilterTeamStatusss() throws Exception {
        // Given
        String sortBy = "기여도순";
        String mainCategoryIds = "1,3";
        String categoryIds = "4,5";
        String taskTitle = "hi";

        // Create mock TeamTaskItemResponse
        TeamTaskItemResponse item1 = new TeamTaskItemResponse(
                1L, "T001", "Task 1", "MainCategory1", "Category1", null, "Requester1", "url1", "Dept1", 1, TaskStatus.IN_PROGRESS, null);

        TeamTaskItemResponse item2 = new TeamTaskItemResponse(
                2L, "T002", "Task 2", "MainCategory2", "Category2", null, "Requester2", "url2", "Dept2", 2, TaskStatus.IN_REVIEWING, null);

        // Create mock TeamTaskResponse
        TeamTaskResponse mockResponse = new TeamTaskResponse(
                1L, "Processor1", "url1", "Dept1", 1, 1, 2, List.of(item1, item2)
        );

        // Mock the service response
        when(teamStatusService.filterTeamStatus(new FilterTeamStatusRequest(sortBy, List.of(1L, 3L), List.of(4L, 5L), taskTitle)))
                .thenReturn(new TeamStatusResponse(List.of(mockResponse)));

        // When + Then
        mockMvc.perform(get("/api/team-status/filter")
                        .param("sortBy", sortBy)
                        .param("mainCategoryIds", mainCategoryIds)
                        .param("categoryIds", categoryIds)
                        .param("taskTitle", taskTitle)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray())  // JSON 응답에서 'members'가 배열인지를 확인
                .andExpect(jsonPath("$.members.length()").value(1))  // 'members' 배열의 길이가 1인지 확인
                .andExpect(jsonPath("$.members[0].tasks.length()").value(2))  // tasks 배열 길이 확인
                .andExpect(jsonPath("$.members[0].tasks[0].taskId").value(1))  // 첫 번째 Task ID 확인
                .andExpect(jsonPath("$.members[0].tasks[1].taskId").value(2));  // 두 번째 Task ID 확인
    }

}


