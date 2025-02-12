package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.response.FilterAssignedTaskListResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.TestDataFactory;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTasksAssignedByManagerTest {

    @Mock
    private MemberService memberService;
    @Mock
    private LoadTaskPort loadTaskPort;
    @InjectMocks
    private FindTaskListService findTaskListService;
    private Member manager;
    private Page<Task> taskPage;
    private Task task1, task2;
    private Category category, mainCategory;
    private Member requester;

    @BeforeEach
    void setUp() {
        manager = TestDataFactory.createManager();
        requester = TestDataFactory.createUser();
        mainCategory = TestDataFactory.createMainCategory();
        category = TestDataFactory.createCategory(mainCategory);
        task1 = TestDataFactory.createTask(1L, "TC001", "제목1", TaskStatus.IN_PROGRESS, category, null, requester);
        task2 = TestDataFactory.createTask(2L, "TC002", "제목2", TaskStatus.IN_REVIEWING, category, LocalDateTime.of(2025, 2, 4, 11, 30, 11), requester);
        taskPage = new PageImpl<>(List.of(task1, task2));
    }

    @Test
    @DisplayName("할당된 작업 목록 조회")
    void findTasksAssignedByManager() {
        // given
        Long managerId = 3L;
        PageRequest pageable = PageRequest.of(0, 20);
        FilterTaskListRequest filterTaskListRequest = new FilterTaskListRequest(null, List.of(), List.of(), "", "", List.of(), "", "");
        when(memberService.findActiveMember(managerId)).thenReturn(manager);
        when(loadTaskPort.findTasksAssignedByManager(managerId, pageable, filterTaskListRequest))
                .thenReturn(taskPage);

        // when
        PageResponse<FilterAssignedTaskListResponse> result = findTaskListService.findTasksAssignedByManager(managerId, pageable, filterTaskListRequest);

        // then
        assertThat(result.content()).hasSize(2)
                .extracting(FilterAssignedTaskListResponse::taskId)
                .containsExactly(1L, 2L);

        FilterAssignedTaskListResponse task1Response = result.content().get(0);
        assertThat(task1Response.taskId()).isEqualTo(1L);
        assertThat(task1Response.taskCode()).isEqualTo("TC001");
        assertThat(task1Response.mainCategoryName()).isEqualTo("1차 카테고리");
        assertThat(task1Response.categoryName()).isEqualTo("2차 카테고리");
        assertThat(task1Response.title()).isEqualTo("제목1");
        assertThat(task1Response.requesterName()).isEqualTo(requester.getNickname());
        assertThat(task1Response.taskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task1Response.finishedAt()).isNull();
    }

    @Test
    @DisplayName("할당된 작업 목록 조회 - 카테고리 필터 적용")
    void findTasksAssignedByManager_FilteredWithCategory() {
        // given
        Long managerId = 10L;
        PageRequest pageable = PageRequest.of(0, 20);
        FilterTaskListRequest filterTaskListRequest = new FilterTaskListRequest(null, List.of(2L), List.of(), "", "", List.of(), "", "");
        taskPage = new PageImpl<>(List.of(task2));

        when(memberService.findActiveMember(managerId)).thenReturn(manager);
        when(loadTaskPort.findTasksAssignedByManager(manager.getMemberId(), pageable, filterTaskListRequest))
                .thenReturn(taskPage);

        // when
        PageResponse<FilterAssignedTaskListResponse> result = findTaskListService.findTasksAssignedByManager(managerId, pageable, filterTaskListRequest);

        // then
        assertThat(result.content()).hasSize(1);
        assertThat(result.content()).extracting(FilterAssignedTaskListResponse::categoryName)
                .containsExactly("2차 카테고리");
    }
}
