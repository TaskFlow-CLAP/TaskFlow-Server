package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.response.FilterRequestedTasksResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
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
class FindTasksRequestedByUserTest {

    @Mock
    private MemberService memberService;
    @Mock
    private LoadTaskPort loadTaskPort;
    @InjectMocks
    private FindTaskListService findTaskListService;

    private FilterTaskListRequest filterTaskListRequest;
    private Member member;
    private Page<Task> taskPage;
    private Task task1, task2;

    @BeforeEach
    void setUp() {
        member = new Member(1L, null, null,
                true, false, true,
                null, MemberStatus.ACTIVE, null
        );
        Category mainCategory = Category.builder()
                .categoryId(1L)
                .name("1차 카테고리")
                .code("VM")
                .isDeleted(false)
                .descriptionExample("메인 카테고리 입니다.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Category category = Category.builder()
                .categoryId(2L)
                .name("2차 카테고리")
                .code("CR")
                .isDeleted(false)
                .descriptionExample("서브 카테고리 입니다.")
                .mainCategory(mainCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        task1 = Task.builder()
                .taskId(1L)
                .taskCode("TC001")
                .title("제목1")
                .description("설명1")
                .category(category)
                .taskStatus(TaskStatus.REQUESTED)
                .finishedAt(null)
                .build();
        task2 = Task.builder()
                .taskId(2L)
                .taskCode("TC002")
                .title("제목2")
                .description("설명2")
                .category(category)
                .taskStatus(TaskStatus.COMPLETED)
                .finishedAt(LocalDateTime.of(2025,2,4,11,30,11))
                .build();

        filterTaskListRequest = new FilterTaskListRequest(null, List.of(), List.of(), "", "", List.of(), "", "");
        taskPage = new PageImpl<>(List.of(task1, task2));
    }

    @Test
    @DisplayName("요청한 작업 목록 조회")
    void findRequestedByUserTasks() {
        //given
        PageRequest pageable = PageRequest.of(0, 20);
        when(memberService.findActiveMember(1L)).thenReturn(member);
        when(loadTaskPort.findTasksRequestedByUser(1L, pageable, filterTaskListRequest))
                .thenReturn(taskPage);
        //when
        PageResponse<FilterRequestedTasksResponse> result = findTaskListService.findTasksRequestedByUser(member.getMemberId(), pageable, filterTaskListRequest);

        //then
        assertThat(result.content()).hasSize(2)
                .extracting(FilterRequestedTasksResponse::taskId)
                .containsExactly(1L, 2L);

        FilterRequestedTasksResponse task1Response = result.content().get(0);
        assertThat(task1Response.taskId()).isEqualTo(1L);
        assertThat(task1Response.taskCode()).isEqualTo("TC001");
        assertThat(task1Response.mainCategoryName()).isEqualTo("1차 카테고리");
        assertThat(task1Response.categoryName()).isEqualTo("2차 카테고리");
        assertThat(task1Response.title()).isEqualTo("제목1");
        assertThat(task1Response.processorName()).isEqualTo("");
        assertThat(task1Response.taskStatus()).isEqualTo(TaskStatus.REQUESTED);
        assertThat(task1Response.finishedAt()).isNull();
    }

    @Test
    @DisplayName("요청한 작업 목록 조회 - 카테고리 조건")
    void findRequestedByUserTasks_FilteredWithCategory() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);
        filterTaskListRequest = new FilterTaskListRequest(null, List.of(2L), List.of(), "", "", List.of(), "", "");
        taskPage = new PageImpl<>(List.of(task2));
        when(memberService.findActiveMember(1L)).thenReturn(member);
        when(loadTaskPort.findTasksRequestedByUser(1L, pageable, filterTaskListRequest))
                .thenReturn(taskPage);

        // when
        PageResponse<FilterRequestedTasksResponse> result = findTaskListService.findTasksRequestedByUser(member.getMemberId(), pageable, filterTaskListRequest);

        // then
        assertThat(result.content()).hasSize(1);
        assertThat(result.content()).extracting(FilterRequestedTasksResponse::categoryName)
                .containsExactly( "2차 카테고리");
    }
}
