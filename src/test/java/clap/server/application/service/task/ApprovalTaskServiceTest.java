package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.TestDataFactory;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.RequestedTaskUpdatePolicy;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApprovalTaskServiceTest {

    @InjectMocks
    private ApprovalTaskService approvalTaskService;

    @Mock
    private MemberService memberService;

    @Mock
    private TaskService taskService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private LabelService labelService;
    @Mock
    private RequestedTaskUpdatePolicy requestedTaskUpdatePolicy;

    @Mock
    private CommandTaskHistoryPort commandTaskHistoryPort;

    @Mock
    private SendNotificationService sendNotificationService;

    @Mock
    private UpdateProcessorTaskCountService updateProcessorTaskCountService;


    private Member reviewer, processor;
    private Task task;
    private Category category, mainCategory;

    @BeforeEach
    void setUp() {
        reviewer = TestDataFactory.createManagerWithReviewer();
        processor = TestDataFactory.createManager();
        mainCategory = TestDataFactory.createMainCategory();
        category = TestDataFactory.createCategory(mainCategory);
        task = TestDataFactory.createTask(1L, "TC001", "제목1", TaskStatus.REQUESTED, category, null, processor);
    }

    @Test
    @DisplayName("작업 승인 처리")
    void approvalTask() {
        //given
        Long reviewerId = 2L;
        Long taskId = 1L;
        ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest(2L, 3L, null, null);

        when(memberService.findReviewer(reviewerId)).thenReturn(reviewer);
        when(taskService.findById(taskId)).thenReturn(task);
        when(memberService.findActiveMemberWithDepartment(approvalTaskRequest.processorId())).thenReturn(processor);
        when(categoryService.findById(approvalTaskRequest.categoryId())).thenReturn(category);
        when(taskService.upsert(task)).thenReturn(task);

        //when
        ApprovalTaskResponse response = approvalTaskService.approvalTaskByReviewer(reviewerId, taskId, approvalTaskRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.taskId()).isEqualTo(task.getTaskId());
        assertThat(response.taskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(requestedTaskUpdatePolicy).validateTaskRequested(task);
    }

    @Test
    @DisplayName("작업 승인 처리 - 담당자의 Task Count 증가")
    void approvalTaskWithIncrementTaskCount() {
        // given
        Long reviewerId = 2L;
        Long taskId = 1L;
        ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest(2L, 3L, null, null);

        when(memberService.findReviewer(reviewerId)).thenReturn(reviewer);
        when(taskService.findById(taskId)).thenReturn(task);
        when(memberService.findActiveMemberWithDepartment(approvalTaskRequest.processorId())).thenReturn(processor);
        when(categoryService.findById(approvalTaskRequest.categoryId())).thenReturn(category);
        when(taskService.upsert(task)).thenReturn(task);

        // when
        ApprovalTaskResponse response = approvalTaskService.approvalTaskByReviewer(reviewerId, taskId, approvalTaskRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.taskId()).isEqualTo(task.getTaskId());
        assertThat(response.taskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        verify(updateProcessorTaskCountService).handleTaskStatusChange(processor, TaskStatus.REQUESTED, TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("작업 승인 처리 중 예외 - 상태 불일치")
    void approvalTask_throwsDomainException_whenTaskStatusIsNotRequested() {
        //given
        Long reviewerId = 2L;
        Long taskId = 1L;
        ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest(2L, 2L, null, null);
        task = TestDataFactory.createTask(1L, "TC001", "제목1", TaskStatus.COMPLETED, category, null, processor);
        when(taskService.findById(taskId)).thenReturn(task);

        //when
        doThrow(new DomainException(TaskErrorCode.TASK_STATUS_MISMATCH))
                .when(requestedTaskUpdatePolicy).validateTaskRequested(task);
        //then
        assertThatThrownBy(() -> approvalTaskService.approvalTaskByReviewer(reviewerId, taskId, approvalTaskRequest))
                .isInstanceOf(DomainException.class)
                .hasMessage(TaskErrorCode.TASK_STATUS_MISMATCH.getMessage());
    }
}
