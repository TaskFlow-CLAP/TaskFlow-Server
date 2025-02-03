package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.response.FilterPendingApprovalResponse;
import clap.server.application.mapper.TaskResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.domain.model.task.Task;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
class FindTaskListServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private LoadTaskPort loadTaskPort;
    @InjectMocks
    private FindTaskListService findTaskListService;

    private FilterTaskListRequest filterTaskListRequest;
    private Pageable pageable;
    private PageResponse<FilterPendingApprovalResponse> expectedResponse;
    private Page<Task> pageResponse;

    //@BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
        filterTaskListRequest = new FilterTaskListRequest(
                null, List.of(2L), List.of(1L), "작업 제목", "", List.of(), "REQUESTED_AT", "DESC"
        );
        Task task1 = Task.builder()
                .taskId(1L)
                .taskCode("TC001")
                .title("작업 제목")
                .dueDate(LocalDateTime.of(2025, 1, 24, 12, 30))
                .build();

        Task task2 = Task.builder()
                .taskId(2L)
                .taskCode("TC002")
                .title("다른 작업 제목")
                .dueDate(LocalDateTime.of(2025, 1, 15, 14, 30))
                .build();


        pageResponse = new PageImpl<>(List.of(task1, task2), pageable, 2);
        expectedResponse = PageResponse.from(pageResponse.map(TaskResponseMapper::toFilterPendingApprovalTasksResponse));
    }

    //@Test
    @DisplayName("승인대기 중인 작업요청목록 조회 - 정상적인 데이터 반환")
    void findPendingApprovalTasks_ReturnFilteredTasks() {
        // given
        Long managerId = 1L;
        when(loadTaskPort.findTasksRequestedByUser(eq(managerId), eq(pageable), eq(filterTaskListRequest)))
                .thenReturn(pageResponse);

        // when
        PageResponse<FilterPendingApprovalResponse> result = findTaskListService.findPendingApprovalTasks(managerId, pageable, filterTaskListRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.content()).hasSize(2)
                .extracting(FilterPendingApprovalResponse::taskId)
                .containsExactly(1L, 2L);
    }

    //@Test
    @DisplayName("승인대기 중인 작업요청목록 조회 - 필터 조건에 맞는 작업 없음")
    void findPendingApprovalTasks_NoTasksFound() {
        // given
        Long managerId = 1L;
        FilterTaskListRequest filterWithNoResults = new FilterTaskListRequest(
                null, List.of(999L), List.of(1000L), "없는 작업 제목", "", List.of(), "REQUESTED_AT", "DESC"
        );
        when(loadTaskPort.findTasksRequestedByUser(eq(managerId), eq(pageable), eq(filterWithNoResults)))
                .thenReturn(Page.empty());

        // when
        PageResponse<FilterPendingApprovalResponse> result = findTaskListService.findPendingApprovalTasks(managerId, pageable, filterWithNoResults);

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalElements()).isEqualTo(0);
        assertThat(result.content()).isEmpty();
    }

    //@Test
    @DisplayName("승인대기 중인 작업요청목록 조회 - 필터 조건에 따른 정확한 결과 반환")
    void findPendingApprovalTasks_FilterByTitle() {
        // given
        Long managerId = 1L;
        FilterTaskListRequest filterByTitle = new FilterTaskListRequest(
                null, List.of(2L), List.of(1L), "작업 제목", "", List.of(), "REQUESTED_AT", "DESC"
        );
        when(loadTaskPort.findTasksRequestedByUser(eq(managerId), eq(pageable), eq(filterByTitle)))
                .thenReturn(pageResponse);

        // when
        PageResponse<FilterPendingApprovalResponse> result = findTaskListService.findPendingApprovalTasks(managerId, pageable, filterByTitle);

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.content())
                .extracting(FilterPendingApprovalResponse::title)
                .containsExactly("작업 제목", "다른 작업 제목");
    }
}
